class_name Level extends Node2D

@onready var player: Player = $Player

func _ready() -> void:
	RenderingServer.set_default_clear_color(Color.BLACK)
	PlayerManager.player = player
	PlayerManager.player_spawned.emit(player)
	LevelManager.current_level = self
	
	player.health = LevelManager.saved_health
	player.health_bar.max_value = player.max_health
	player.health_bar.value = player.health
	
	player.diamonds = 0
	player.diamonds_bar.value = 0
