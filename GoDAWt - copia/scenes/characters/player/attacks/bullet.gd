class_name Bullet extends Area2D

@export var bullet_speed: float = 700.0
@onready var hurtbox: Area2D = $Hurtbox

var direction: int = 1

func _ready() -> void:
	hurtbox.damage = 4
	hurtbox.connect("area_entered", Callable(self, "_on_hurtbox_area_entered"))
	

func _process(delta: float) -> void:
	position.x += bullet_speed * delta * direction


func _on_hurtbox_area_entered(area: Area2D) -> void:
	var enemy_node = area.get_parent()
	if enemy_node.is_in_group("Enemies") and not enemy_node.get("is_dead"):
		_destroy()

func _destroy() -> void:
	queue_free()
