extends StaticBody2D

@export var speed: float = 120.0
@onready var detection_area: Area2D = $Area2D

var player_on_platform: bool = false

func _ready():
	detection_area.body_entered.connect(_on_body_entered)
	detection_area.body_exited.connect(_on_body_exited)

func _physics_process(delta):
	if player_on_platform:
		position.x -= speed * delta
	else:
		position.x += speed * delta * 0.4

	if position.x < 100:
		position.x = 100
	elif position.x > 3700:
		position.x = 3700

func _on_body_entered(body):
	if body is BasePlayer:
		player_on_platform = true

func _on_body_exited(body):
	if body is BasePlayer:
		player_on_platform = false
