extends BaseEnemy

@onready var detection_component: DetectionComponent = $DetectionComponent
@onready var ray_cast: RayCast2D = $RayCast2D
@onready var attack_hurtbox: Area2D = $Sprite2D/AttackHurtbox
@onready var attack_hurtbox_shape: CollisionShape2D = $Sprite2D/AttackHurtbox/CollisionShape2D

@export var speed: float = 450.0
@export var attack_range: float = 30.0
@export var attack_duration: float = 1.0
@export var hit_delay: float = 0.3


var is_attacking: bool = false
var player_detected: bool = false


func _ready() -> void:
	detection_component.player_entered.connect(_on_player_entered)
	detection_component.player_exited.connect(_on_player_exited)
	hitbox.take_damage.connect(_on_take_damage)
	if PlayerManager.player:
		PlayerManager.player.died.connect(_on_player_died)


func _physics_process(_delta: float) -> void:
	if not PlayerManager.player:
		return

	var player_pos = PlayerManager.player.global_position
	var distance = global_position.distance_to(player_pos)
	var direction_to_player = sign(player_pos.x - global_position.x)

	if not is_attacking:
		sprite.flip_h = direction_to_player < 0
		attack_hurtbox.scale.x = -1.1 if sprite.flip_h else 1.0

		ray_cast.position.x = abs(ray_cast.position.x) * direction_to_player

	if is_attacking:
		velocity = Vector2.ZERO
	else:
		if player_detected:
			if distance <= attack_range:
				_start_attack()
			elif ray_cast.is_colliding():
				velocity.x = direction_to_player * speed
				animation_player.play("run")
			else:
				velocity = Vector2.ZERO
				animation_player.play("idle")
		else:
			velocity = Vector2.ZERO
			animation_player.play("idle")

	move_and_slide()


func _on_player_entered() -> void:
	player_detected = true

func _on_player_exited() -> void:
	player_detected = false

func _start_attack() -> void:
	is_attacking = true
	velocity = Vector2.ZERO

	var direction_to_player = sign(PlayerManager.player.global_position.x - global_position.x)
	sprite.flip_h = direction_to_player < 0
	attack_hurtbox.scale.x = -1.2 if sprite.flip_h else 1.2

	animation_player.play("attack")

	await get_tree().create_timer(hit_delay).timeout
	attack_hurtbox.monitoring = true
	attack_hurtbox.visible = true
	attack_hurtbox_shape.disabled = false

	await animation_player.animation_finished

	attack_hurtbox.monitoring = false
	attack_hurtbox.visible = false
	attack_hurtbox_shape.disabled = true
	is_attacking = false


func _on_player_died() -> void:
	player_detected = false
	is_attacking = false
	velocity = Vector2.ZERO

	attack_hurtbox.set_deferred("monitoring", false)
	attack_hurtbox.set_deferred("visible", false)
	attack_hurtbox_shape.call_deferred("set_disabled", true)

	animation_player.play("idle")
	set_physics_process(false)
