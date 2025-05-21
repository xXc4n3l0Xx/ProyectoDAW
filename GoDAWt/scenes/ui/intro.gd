extends CanvasLayer

@onready var images = [
	$TextureRect,
	$TextureRect2,
	$TextureRect3,
	$TextureRect4
]
@onready var timer = $Timer

var current_index = 0

func _ready() -> void:
	for i in images.size():
		images[i].visible = i == 0
	
	timer.wait_time = 3.0
	timer.connect("timeout", _on_timer_timeout)
	timer.start()

func _on_timer_timeout() -> void:
	images[current_index].visible = false
	current_index += 1
	
	if current_index >= images.size():
		get_tree().change_scene_to_file("res://scenes/ui/main_menu/main_menu.tscn")
	else:
		images[current_index].visible = true
		timer.start()
