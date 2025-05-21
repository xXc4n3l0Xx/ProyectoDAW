extends Node

signal level_load_started
signal level_load_finished

var current_level: Node
var next_level: Node
const STARTING_LEVEL_PATH: String = "res://scenes/levels/level00.tscn"

var saved_health: int = 10
var saved_score: int = 0

var current_level_path: String = ""


func _ready() -> void:
	await get_tree().process_frame
	level_load_finished.emit()

func change_level(level_path: String) -> void:
	if not is_valid_level_path(level_path):
		return

	save_progress_if_alive()

	get_tree().paused = true
	level_load_started.emit()
	await SceneTransition.fade_out()

	for child in get_tree().root.get_children():
		if child is Level:
			child.queue_free()
		elif "scene_file_path" in child and child.scene_file_path == "res://scenes/ui/final.tscn":
			child.queue_free()

	await get_tree().process_frame

	next_level = load_new_level(level_path)
	if not next_level:
		printerr("Failed to load level: ", level_path)
		get_tree().paused = false
		level_load_finished.emit()
		return
	current_level_path = level_path

	add_new_level()

	await SceneTransition.fade_in()
	get_tree().paused = false
	level_load_finished.emit()

func save_progress_if_alive() -> void:
	if PlayerManager.player and PlayerManager.player.health > 0:
		saved_score = PlayerManager.player.score
		saved_health = PlayerManager.player.health

func is_valid_level_path(level_path: String) -> bool:
	return level_path and FileAccess.file_exists(level_path)

func load_new_level(level_path: String) -> Node:
	var level_scene = load(level_path) as PackedScene
	if not level_scene:
		return null

	var instance = level_scene.instantiate()
	return instance if instance else null

func add_new_level() -> void:
	get_tree().root.add_child(next_level)
	current_level = next_level

func restart_current_level() -> void:
	if current_level:
		if PlayerManager.player and PlayerManager.player.health <= 0:
			saved_health = PlayerManager.player.max_health
		if "scene_file_path" in current_level:
			change_level(current_level.scene_file_path)
