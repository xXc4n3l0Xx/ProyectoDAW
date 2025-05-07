class_name ThrowState extends State


@onready var run_state: RunState = $"../RunState"
@onready var idle_state: IdleState = $"../IdleState"
@onready var fall_state: FallState = $"../FallState"
@onready var jump_state: JumpState = $"../JumpState"
@onready var bullet_scene: PackedScene = preload("res://scenes/characters/player/attacks/bullet.tscn")
@onready var knockback_state: KnockbackState = $"../KnockbackState"


func enter() -> void:
	player.gun_sound.play()
	_shoot()
	if player.is_on_floor():
		player.velocity.x = 0
	player.animation_player.play("throw")
	await player.animation_player.animation_finished


func exit() -> void:
	pass


func process(_delta: float) -> State:
	if player.animation_player.is_playing():
		return
	else:
		if player.direction != 0:
			return run_state
		else:
			return idle_state


func physics(_delta: float) -> State:
	if player.is_knocked_back:
		return knockback_state
	return null


func unhandled_input(event: InputEvent) -> State:
	if event.is_action_pressed("jump") and player.is_on_floor():
		return jump_state
	return null


func _shoot() -> void:	
	if owner is Player and owner.has_node("Bullets"):
		var bullet: Bullet = bullet_scene.instantiate()
		var bullet_position: Vector2 = player.attack.global_position
	
		player.get_node("Bullets").add_child(bullet)
		bullet.direction = -1 if player.sprite.flip_h else 1
		bullet.global_position = bullet_position
