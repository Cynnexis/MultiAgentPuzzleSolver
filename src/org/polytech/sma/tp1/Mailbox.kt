package org.polytech.sma.tp1

object Mailbox {
	
	private val messages: Array<Message?> = arrayOfNulls<Message?>(Grid.MAX_AGENT)
	
	@Synchronized
	fun add(message: Message) {
		messages[message.receiver.id] = message
	}
	
	@Synchronized
	fun get(receiver: Agent): Message? {
		return messages[receiver.id]
	}
	
	@Synchronized
	fun pop(receiver: Agent): Message? {
		val message: Message? = get(receiver)
		if (message != null)
			messages[receiver.id] = null
		return message
	}
}