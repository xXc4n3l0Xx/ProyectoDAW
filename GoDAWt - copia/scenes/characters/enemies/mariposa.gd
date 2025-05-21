extends BaseEnemy

@onready var fly_sound: AudioStreamPlayer2D = $FlySound

@export var top_offset: float = -250.0
@export var bottom_offset: float = 250.0
@export var pause_time: float = 1

var origin_y: float
var target_y: float
var moving_down := true
var pause_timer: float

func _ready() -> void:
	move_speed = 200
	origin_y = global_position.y
	target_y = origin_y + bottom_offset
	hitbox.take_damage.connect(_on_take_damage)

func _physics_process(delta: float) -> void:
	const TOLERANCE := 1.0

	if pause_timer > 0.0:
		pause_timer -= delta
		velocity.y = 0
		if fly_sound.playing:
			fly_sound.stop()
	else:
		var direction : float = sign(target_y - global_position.y)
		velocity.y = direction * move_speed
		if not fly_sound.playing:
			fly_sound.play()

		if moving_down and global_position.y >= target_y - TOLERANCE:
			moving_down = false
			pause_timer = pause_time
			origin_y = global_position.y
			target_y = origin_y + top_offset

		elif not moving_down and global_position.y <= target_y + TOLERANCE:
			moving_down = true
			pause_timer = pause_time
			origin_y = global_position.y
			target_y = origin_y + bottom_offset

	move_and_slide()
