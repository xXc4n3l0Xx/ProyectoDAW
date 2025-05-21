class_name JumpState extends State

@onready var fall_state: FallState = $"../FallState"
@onready var melee_state: MeleeState = $"../MeleeState"
@onready var throw_state: ThrowState = $"../ThrowState"
@onready var knockback_state: KnockbackState = $"../KnockbackState"


func enter() -> void:
	player.jump_sound.play()
	player.velocity.y = player.jump_force
	player.animation_player.play("jump")


func exit() -> void:
	pass

func process(_delta: float) -> State:
	return null

func physics(_delta: float) -> State:
	if player.is_knocked_back:
		return knockback_state
	if player.direction:
		player.velocity.x = lerp(player.velocity.x, player.direction * player.speed, player.air_acceleration)
	else:
		player.velocity.x = lerp(player.velocity.x, 0.0, player.air_acceleration * 0.5)

	if player.velocity.y >= 0:
		return fall_state
	return null



func unhandled_input(event: InputEvent) -> State:
	if event.is_action_pressed("melee"):
		return melee_state
	if event.is_action_pressed("throw"):
		return throw_state

	return null
