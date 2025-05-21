extends Node2D

@onready var player: BasePlayer = $Player
@onready var level_transition = $LevelTransition

func _ready() -> void:
	RenderingServer.set_default_clear_color(Color.BLACK)

	player.health = GameState.saved_health
	player.score = GameState.saved_score

	player.health_bar.max_value = player.max_health
	player.health_bar.value = player.health
	player.diamonds = 0
	player.diamonds_bar.max_value = player.max_diamonds
	player.diamonds_bar.value = 0
	player.update_score_label()

	PlayerManager.player = player
	PlayerManager.player_spawned.emit(player)

	level_transition.body_entered.connect(_on_portal_entered)

func _on_portal_entered(body):
	if body is BasePlayer:
		call_deferred("go_to_next_level")

func go_to_next_level():
	player.set_physics_process(false)
	player.set_process(false)
	player.score += 24000
	player.update_score_label()

	GameState.saved_health = player.health
	GameState.saved_score = player.score

	player.report_score()

	await SceneTransition.fade_out()
	get_tree().change_scene_to_file("res://scenes/levels/level06.tscn")
	await SceneTransition.fade_in()
