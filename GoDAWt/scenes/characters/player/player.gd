class_name Player extends BasePlayer

@onready var state_machine: StateMachine = $StateMachine



func _ready() -> void:
	super._ready()
	PlayerManager.player = self
	state_machine.configure(self)
