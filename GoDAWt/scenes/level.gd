class_name Level extends Node2D

@onready var player: Player = $Player

func _ready() -> void:
	RenderingServer.set_default_clear_color(Color.BLACK)
