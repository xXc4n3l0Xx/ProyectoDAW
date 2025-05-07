class_name Hitbox extends Area2D


signal take_damage(amount: int)

func _on_area_entered(area: Area2D) -> void:
	if area is Hurtbox:
		take_damage.emit(area.damage)
