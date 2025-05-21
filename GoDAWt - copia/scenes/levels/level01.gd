extends Node2D

@onready var level_transition: LevelTransition = $LevelTransition

func _ready():
	level_transition.body_entered.connect(_on_portal_entered)

func _on_portal_entered(body):
	if body is Player:
		print("→ Pasando al siguiente nivel...")
		get_tree().change_scene_to_file("res://scenes/levels/level02.tscn")
