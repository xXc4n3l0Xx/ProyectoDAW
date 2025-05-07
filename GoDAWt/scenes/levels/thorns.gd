class_name Thorns extends Area2D

@onready var hurtbox: Hurtbox = $Hurtbox

func _ready() -> void:
	hurtbox.damage = 500


func _process(_delta: float) -> void:
	pass
