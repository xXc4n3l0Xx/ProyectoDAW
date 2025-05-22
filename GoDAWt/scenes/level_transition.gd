class_name LevelTransition
extends Area2D

@onready var collision_shape: CollisionShape2D = $CollisionShape2D
@onready var sprite: Sprite2D = $CollisionShape2D/Sprite2D

signal portal_touched

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
		emit_signal("portal_touched")
