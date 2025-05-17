class_name LevelTransition extends Area2D

@onready var collision_shape: CollisionShape2D = $CollisionShape2D
@onready var sprite: Sprite2D = $CollisionShape2D/Sprite2D

func _ready() -> void:
	collision_shape.disabled = true
	sprite.visible = false

func _process(_delta: float) -> void:
	if PlayerManager.player and PlayerManager.player.diamonds_bar.value >= 5:
		collision_shape.disabled = false
		sprite.visible = true
	else:
		collision_shape.disabled = true
		sprite.visible = false



func _on_body_entered(body: Node2D) -> void:
	if body is Player:
		var current_path: String = LevelManager.current_level.scene_file_path
		var next_path: String = calculate_next_level_path(current_path)
		if next_path:
			LevelManager.change_level(next_path)

func calculate_next_level_path(current_path: String) -> String:
	var regex = RegEx.new()
	regex.compile(r"level(\d+)\.tscn")
	var result = regex.search(current_path)
	if result:
		var level_number = result.get_string(1).to_int()
		if level_number < 6:
			level_number += 1
		var next_level_str = str(level_number).pad_zeros(2)
		return "res://scenes/levels/level%s.tscn" % next_level_str
	else:
		printerr("No se pudo determinar el número de nivel en el path: ", current_path)
		return ""
