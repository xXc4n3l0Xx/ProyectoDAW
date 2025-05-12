extends CanvasLayer

@onready var animation_player: AnimationPlayer = $AnimationPlayer
@onready var audio_stream_player: AudioStreamPlayer2D = $AudioStreamPlayer2D


func _ready() -> void:
	process_mode = Node.PROCESS_MODE_ALWAYS


func fade_out() -> bool:
	animation_player.play("fade_out")
	await animation_player.animation_finished
	return true
	
	
func fade_in() -> bool:
	audio_stream_player.play()
	animation_player.play("fade_in")
	await animation_player.animation_finished
	return true
