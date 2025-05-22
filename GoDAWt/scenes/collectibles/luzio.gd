class_name Luzio extends Area2D

signal collected

@onready var sprite: Sprite2D = $Sprite2D
@onready var collision_shape: CollisionShape2D = $CollisionShape2D
@onready var audio_stream_player: AudioStreamPlayer = $AudioStreamPlayer

func _ready() -> void:
	body_entered.connect(_on_body_entered)

func _on_body_entered(body: Node2D) -> void:
	if body is Player:
		collect(body)

func collect(_player: Player) -> void:
	collected.emit()
	PlayerManager.player.score += randi_range(15000, 15030)
	PlayerManager.player.update_score_label()
	audio_stream_player.play()
	collision_shape.set_deferred("disabled", true)
	sprite.visible = false
	await audio_stream_player.finished
	queue_free()
