extends CanvasLayer

func _ready() -> void:
	await get_tree().process_frame

	PlayerManager.player_spawned.connect(_on_player_spawned)

	visible = false

func _on_player_spawned(player: BasePlayer) -> void:
	if player.died.is_connected(_on_player_died):
		player.died.disconnect(_on_player_died)

	player.died.connect(_on_player_died)

func _on_player_died() -> void:
	visible = true
	get_tree().paused = true

func _on_restart_button_pressed() -> void:
	var current_scene_path = get_tree().current_scene.scene_file_path
	get_tree().change_scene_to_file(current_scene_path)
	get_tree().paused = false
	visible = false

func _on_quit_button_pressed() -> void:
	visible = false
	get_tree().paused = false
	get_tree().change_scene_to_file("res://scenes/ui/main_menu/main_menu.tscn")
