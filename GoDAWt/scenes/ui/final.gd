extends CanvasLayer

@onready var images = [
	$TextureRect,
]

@onready var final_sound = $AudioStreamPlayer

var current_index = 0

func _ready() -> void:
	for i in images.size():
		images[i].visible = i == 0
	
	final_sound.play()

	final_sound.finished.connect(_on_audio_finished)

func _on_audio_finished() -> void:
	get_tree().change_scene_to_file("res://scenes/ui/main_menu/main_menu.tscn")
