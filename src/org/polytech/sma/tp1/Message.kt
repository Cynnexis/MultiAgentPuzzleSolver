package org.polytech.sma.tp1

data class Message(
	val emitter: Agent? = null,
	val receiver: Agent,
	val movementWhereEmitterIs: Movement = Movement.STAY,
	val read: Boolean = false
) {
}