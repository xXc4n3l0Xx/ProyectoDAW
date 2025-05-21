class_name RunnerPlayer extends Player

@export var runner_speed: float = 400.0

func _ready() -> void:
	super._ready()
	PlayerManager.player = self

func _process(_delta):
	pass

func _physics_process(delta: float) -> void:
	velocity.x = runner_speed

	if not is_on_floor():
		velocity.y += gravity * delta

	if is_on_floor() and Input.is_action_just_pressed("jump"):
		velocity.y = jump_force
		jump_sound.play()

	move_and_slide()
