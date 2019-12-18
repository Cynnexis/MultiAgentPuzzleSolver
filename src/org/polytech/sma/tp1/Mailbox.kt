package org.polytech.sma.tp1

object Mailbox {
	
	private val messages = HashMap<Int, Message?>()
	
	@Synchronized
	fun send(message: Message) {
		messages[message.receiver.id] = message
	}
	@Synchronized
	fun send(receiver: Agent, message: Message) {
		val m = Message(message.emitter, receiver, message.movementWhereEmitterIs)
		messages[receiver.id] = m
	}
	
	@Synchronized
	fun getMessage(receiver: Agent): Message? {
		return messages[receiver.id]
	}
	
	@Synchronized
	fun pop(receiver: Agent): Message? {
		val message: Message? = get(receiver)
		if (message != null)
			messages[receiver.id] = null
		return message
	}
	
	@Synchronized
	operator fun get(receiver: Agent): Message? {
		return getMessage(receiver)
	}
	
	@Synchronized
	operator fun set(receiver: Agent, message: Message) {
		send(receiver, message)
	}
	
	@Synchronized
	operator fun invoke(receiver: Agent): Message? {
		return pop(receiver)
	}
}