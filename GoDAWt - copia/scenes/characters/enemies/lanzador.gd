extends BaseEnemy

@onready var detection_component: DetectionComponent = $DetectionComponent
@onready var marker: Marker2D = $Marker
@onready var rock_scene: PackedScene = preload("res://scenes/characters/enemies/attacks/rock.tscn")


@export var throw_force: float = 400.0

var player_detected: bool = false
var can_attack: bool = true


func _ready() -> void:
	detection_component.player_entered.connect(_on_player_entered)
	detection_component.player_exited.connect(_on_player_exited)
	hitbox.take_damage.connect(_on_take_damage)
	if PlayerManager.player:
		PlayerManager.player.died.connect(_on_player_died)

func _on_player_entered() -> void:
	player_detected = true
	_try_throw_rock()

func _on_player_exited() -> void:
	player_detected = false

func _try_throw_rock() -> void:
	if not player_detected or not can_attack or not PlayerManager.player or is_dead:
		return

	can_attack = false
	animation_player.play("attack")

	await get_tree().create_timer(0.3).timeout
	if is_dead: return

	var rock = rock_scene.instantiate()
	get_parent().add_child(rock)
	rock.global_position = marker.global_position
	rock.origin_position = global_position

	var target = PlayerManager.player.global_position
	var offset = Vector2(0, -50)
	var dir = (target + offset - marker.global_position).normalized()

	var distance = marker.global_position.distance_to(target)
	var force = clamp(distance * 3, 200, 800)

	var throw_velocity = dir * force
	rock.launch(throw_velocity)

	await animation_player.animation_finished
	if is_dead: return

	animation_player.play("idle")

	await get_tree().create_timer(2.0).timeout
	if is_dead: return

	can_attack = true

	if player_detected and not is_dead:
		_try_throw_rock()


func _on_player_died() -> void:
	player_detected = false
	can_attack = false
