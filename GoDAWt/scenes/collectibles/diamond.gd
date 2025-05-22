class_name Diamond extends Area2D

signal collected

@onready var sprite: Sprite2D = $Sprite2D
@onready var collision_shape: CollisionShape2D = $CollisionShape2D
@onready var animation_player: AnimationPlayer = $AnimationPlayer
@onready var audio_stream_player: AudioStreamPlayer = $AudioStreamPlayer

func _ready() -> void:
	body_entered.connect(_on_body_entered)


func _on_body_entered(body: Node2D) -> void:
	if body is Player:
		collect(body)

func collect(_player: Player) -> void:
	collected.emit()
	audio_stream_player.play()
	PlayerManager.player.diamonds += 1
	PlayerManager.player.score += randi_range(2500, 2530)
	PlayerManager.player.update_score_label()
	PlayerManager.player.diamonds_bar.value = PlayerManager.player.diamonds
	collision_shape.set_deferred("disabled", true)
	sprite.visible = false
	await audio_stream_player.finished
	queue_free()
