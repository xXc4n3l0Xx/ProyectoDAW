class_name RunState extends State

@onready var idle_state: IdleState = $"../IdleState"
@onready var jump_state: JumpState = $"../JumpState"
@onready var fall_state: FallState = $"../FallState"
@onready var melee_state: MeleeState = $"../MeleeState"
@onready var throw_state: ThrowState = $"../ThrowState"
@onready var knockback_state: KnockbackState = $"../KnockbackState"



func enter() -> void:
	player.animation_player.play("run")


func exit() -> void:
	pass



func process(_delta: float) -> State:
	if player.direction == 0:
		return idle_state
	return null



func physics(_delta: float) -> State:
	player.velocity.x = player.direction * player.speed

	if not player.is_on_floor():
		return fall_state
	if player.is_knocked_back:
		return knockback_state

	return null



func unhandled_input(event: InputEvent) -> State:
	if event.is_action_pressed("jump") and player.is_on_floor():
		return jump_state
	if event.is_action_pressed("throw") and player.is_on_floor():
		return throw_state
	if event.is_action_pressed("melee") and player.is_on_floor():
		return melee_state
	return null
