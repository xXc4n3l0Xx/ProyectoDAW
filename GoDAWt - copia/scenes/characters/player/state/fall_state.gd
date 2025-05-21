class_name FallState extends State

@onready var idle_state: IdleState = $"../IdleState"
@onready var run_state: RunState = $"../RunState"
@onready var melee_state: MeleeState = $"../MeleeState"
@onready var throw_state: ThrowState = $"../ThrowState"
@onready var knockback_state: KnockbackState = $"../KnockbackState"



func enter() -> void:
	player.animation_player.play("fall")


func exit() -> void:
	pass


func process(_delta: float) -> State:
	return null



func physics(_delta: float) -> State:
	if player.direction:
		player.velocity.x = lerp(player.velocity.x, player.direction * player.speed, player.air_acceleration)
	else:
		player.velocity.x = lerp(player.velocity.x, 0.0, player.air_acceleration * 0.5)

	if player.is_knocked_back:
		return knockback_state
	if player.is_on_floor():
		return idle_state
	return null



func unhandled_input(event: InputEvent) -> State:
	if event.is_action_pressed("melee"):
		return melee_state
	if event.is_action_pressed("throw"):
		return throw_state

	return null
