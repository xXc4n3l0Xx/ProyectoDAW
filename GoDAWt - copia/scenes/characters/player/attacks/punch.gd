class_name Punch extends Area2D


@onready var hurtbox: Hurtbox = $Hurtbox


func _ready() -> void:
	hurtbox.damage = 5
	await get_tree().create_timer(0.1).timeout
	queue_free()

func _process(_delta: float) -> void:
	pass

func _destroy() -> void:
	queue_free()
