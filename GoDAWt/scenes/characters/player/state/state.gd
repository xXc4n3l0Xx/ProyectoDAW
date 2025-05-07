class_name State extends Node

static var player: Player
static var state_machine: StateMachine



func enter() -> void:
	pass


func exit() -> void:
	pass



func process(_delta: float) -> State:
	return null



func physics_process(_delta: float) -> State:
	return null



func unhandled_input(_event: InputEvent) -> State:
	return null


func can_change_state() -> bool:
	return true
