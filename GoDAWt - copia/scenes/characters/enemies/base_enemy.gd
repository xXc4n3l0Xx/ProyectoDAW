class_name BaseEnemy extends CharacterBody2D


@export var health: int = 20
@export var move_speed: float = 400.0
var is_dead: bool = false


@onready var animation_player: AnimationPlayer = $AnimationPlayer
@onready var hitbox: Hitbox = $Hitbox
@onready var death_sound: AudioStreamPlayer2D = $DeathSound
@onready var hurtbox: Hurtbox = $Hurtbox
@onready var sprite: Sprite2D = $Sprite2D


func _on_take_damage(amount: int) -> void:
	if is_dead:
		return
	health -= amount
	_play_damage_effect()
	if health <= 0:
		is_dead = true
		die()


func die() -> void:
	is_dead = true
	set_physics_process(false)
	visible = false

	if Hurtbox:
		hurtbox.call_deferred("set", "monitoring", false)
		hurtbox.call_deferred("set", "monitorable", false)
		hurtbox.set_collision_layer_value(5, false)
		hurtbox.set_collision_mask_value(1, false)

	if hitbox:
		hitbox.call_deferred("set", "monitoring", false)
		hitbox.call_deferred("set", "monitorable", false)
		hitbox.set_collision_layer_value(5, false)
		hitbox.set_collision_mask_value(1, false)

	death_sound.play()
	await death_sound.finished
	queue_free()

func _play_damage_effect() -> void:
	var tween: Tween = create_tween()
	tween.tween_property(sprite, "modulate:v", 1, 0.25).from(15)


func is_enemy_dead() -> bool:
	return is_dead
