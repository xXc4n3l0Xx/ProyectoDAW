class_name BasePlayer extends CharacterBody2D

@onready var sprite: Sprite2D = $Sprite2D
@onready var animation_player: AnimationPlayer = $AnimationPlayer
@onready var attack: Marker2D = $Attack
@onready var hitbox: Hitbox = $Hitbox
@onready var bullets: Node = $Bullets
@onready var punch: Node = $Punch
@onready var death_sound: AudioStreamPlayer = $Sounds/DeathSound
@onready var gun_sound: AudioStreamPlayer = $Sounds/GunSound
@onready var punch_sound: AudioStreamPlayer = $Sounds/PunchSound
@onready var jump_sound: AudioStreamPlayer = $"Sounds/JumpSound"
@onready var thorns_ray: RayCast2D = $ThornsRay
@onready var knockback_state: KnockbackState = $StateMachine/KnockbackState
@onready var hurt_sound: AudioStreamPlayer = $Sounds/HurtSound
@onready var health_bar: TextureProgressBar = $HealthCanvasLayer/HealthBar
@onready var diamonds_bar: TextureProgressBar = $DiamondsCanvasBar/DiamondsBar
@onready var score_label: Label = $PointsCanvas/ScoreLabel

@export_category("Physics")
@export var speed: float = 200.0
@export var gravity: float = 980.0
@export var air_acceleration: float = 0.6
@export var jump_force: float = -400.0

@export_category("Attack")
@export var max_bullets: int = 3

@export_category("Health")
@export var max_health: int = 10
@export var health: int = 10

var direction: float
var is_dead: bool = false
var is_knocked_back: bool = false
var is_invulnerable: bool = false
var invulnerable_timer: float = 0.0
var max_diamonds: int = 5
var diamonds: int = 0
var score: int = 0
signal died

func _ready() -> void:
	hitbox.take_damage.connect(_on_take_damage)
	health_bar.value = health
	health_bar.max_value = max_health
	diamonds = 0
	diamonds_bar.value = diamonds
	diamonds_bar.max_value = max_diamonds
	update_score_label()

func _process(_delta: float) -> void:
	direction = Input.get_axis("left", "right")
	if direction:
		set_player_direction()
	manage_bullets()
	if is_invulnerable:
		invulnerable_timer -= _delta
		if invulnerable_timer <= 0:
			is_invulnerable = false

func _physics_process(delta: float) -> void:
	if not is_on_floor():
		velocity.y += gravity * delta
	move_and_slide()

	if thorns_ray.is_colliding():
		force_die()

func force_die() -> void:
	if not is_dead:
		LevelManager.saved_health = health
		health = 0
		die()

func set_player_direction() -> void:
	sprite.flip_h = direction < 0
	attack.position.x = -24 if direction < 0 else 24

func manage_bullets() -> void:
	for bullet: Bullet in bullets.get_children():
		if abs(bullet.global_position.x - global_position.x) > 500:
			bullet._destroy()

func _on_take_damage(amount: int) -> void:
	if is_knocked_back or is_dead or is_invulnerable:
		return

	hurt_sound.play()
	health_bar.value -= amount
	health -= amount
	if health <= 0:
		is_dead = true
		die()
	else:
		knockback_state.enter()

func update_score_label() -> void:
	score_label.text = "Score: %d" % score

func report_score():
	if not Engine.has_singleton("JavaScript"):
		return

	var js_code = """
		(function() {
			const score = %d;
			const token = localStorage.getItem("jwt");
			if (!token) return;

			fetch("http://localhost:8080/api/usuarios/score", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
					"Authorization": "Bearer " + token
				},
				body: JSON.stringify(score)
			});
		})();
	""" % score

	Engine.get_singleton("JavaScript").eval(js_code, true)


func die() -> void:
	is_dead = true
	set_physics_process(false)
	died.emit()
	visible = false

	score = clamp(LevelManager.saved_score - 1234, 0, 999999)
	update_score_label()

	LevelManager.saved_score = score

	PlayerManager.player = self
	PlayerManager.player_spawned.emit(self)

	if hitbox:
		hitbox.set_deferred("monitoring", false)
		hitbox.set_deferred("monitorable", false)
		hitbox.set_collision_layer_value(1, false)
		hitbox.set_collision_mask_value(1, false)

	death_sound.play()
