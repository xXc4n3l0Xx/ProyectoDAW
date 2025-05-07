extends Node

const PLAYER = preload("res://scenes/characters/player/player.tscn")

var player: Player

func _ready() -> void:
	player = PLAYER.instantiate()
	await get_tree().create_timer(0.3).timeout
	
