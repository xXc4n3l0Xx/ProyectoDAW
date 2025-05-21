class_name MeleeState extends State


@onready var idle_state: IdleState = $"../IdleState"
@onready var run_state: RunState = $"../RunState"
@onready var fall_state: FallState = $"../FallState"
@onready var jump_state: JumpState = $"../JumpState"
@onready var punch_scene: PackedScene = preload("res://scenes/characters/player/attacks/punch.tscn")
@onready var knockback_state: KnockbackState = $"../KnockbackState"


func enter() -> void:
	if not player.is_dead:
		player.punch_sound.play()
		_punch()
		if player.is_on_floor():
			player.velocity.x = 0
		player.animation_player.play("melee")
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


func _punch() -> void:
	if owner.has_node("Punch"):
		var punch: Punch = punch_scene.instantiate()
		var punch_position: Vector2 = player.attack.global_position

		player.get_node("Punch").add_child(punch)
		punch.global_position = punch_position
