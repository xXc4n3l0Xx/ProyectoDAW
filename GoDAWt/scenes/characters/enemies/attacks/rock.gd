class_name Rock extends Area2D

@export var rock_gravity: float = 1000.0
@export var max_distance: float = 500.0

@onready var bonk_sound: AudioStreamPlayer2D = $BonkSound
@onready var hurtbox: Area2D = $Hurtbox
@onready var animated_sprite: AnimatedSprite2D = $AnimatedSprite2D


var velocity: Vector2 = Vector2.ZERO
var origin_position: Vector2


func _ready() -> void:
	hurtbox.damage = 3
	hurtbox.connect("area_entered", Callable(self, "_on_hurtbox_area_entered"))


func _physics_process(delta: float) -> void:
	velocity.y += rock_gravity * delta
	position += velocity * delta

	if global_position.distance_to(origin_position) > max_distance:
		_destroy()


func launch(initial_velocity: Vector2) -> void:
	velocity = initial_velocity


func _on_hurtbox_area_entered(_area: Area2D) -> void:
	animated_sprite.visible = false
	bonk_sound.play()
	hurtbox.set_deferred("monitoring", false)
	await bonk_sound.finished
	_destroy()


func _destroy() -> void:
	queue_free()
