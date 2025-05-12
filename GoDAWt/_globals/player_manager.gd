extends Node

signal player_spawned(player)

var player: Player

func _ready() -> void:
	pass

func _on_current_scene_changed(scene: Node) -> void:
	var current_scene_path = scene.scene_file_path
	
	if current_scene_path != "res://scenes/ui/main_menu/main_menu.tscn":
		player = scene.get_node_or_null("Player")
		if player:
			emit_signal("player_spawned", player)
