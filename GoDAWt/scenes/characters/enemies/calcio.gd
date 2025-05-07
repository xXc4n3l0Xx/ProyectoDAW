extends BaseEnemy

@onready var timer: Timer = $ChargeTimer

@export var pause_duration: float = 1.5
@export var charge_distance: float = 500.0

var player: Node2D = null
var charging: bool = false
var charge_direction: Vector2
var charge_start_position: Vector2

func _ready() -> void:
	player = get_node("../Player")
	timer.wait_time = pause_duration
	timer.one_shot = true
	timer.timeout.connect(_on_timer_timeout)
	timer.start()

func _physics_process(_delta: float) -> void:
	if charging:
		move_and_slide()
		if global_position.distance_to(charge_start_position) >= charge_distance:
			charging = false
			velocity = Vector2.ZERO
			timer.start()
	else:
		velocity = Vector2.ZERO

func _on_timer_timeout() -> void:
	if player:
		charge_direction = (player.global_position - global_position).normalized()
		velocity = charge_direction * move_speed
		charge_start_position = global_position
		charging = true
