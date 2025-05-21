class_name MainMenu extends Control

@onready var button_container: VBoxContainer = $ButtonContainer

func _on_start_button_pressed() -> void:
	GameState.saved_score = 0
	GameState.saved_health = 10
	get_tree().change_scene_to_file("res://scenes/levels/level00.tscn")


func _on_credits_button_pressed() -> void:
	pass
