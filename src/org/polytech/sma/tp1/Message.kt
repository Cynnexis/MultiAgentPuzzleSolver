package org.polytech.sma.tp1

import java.util.*

data class Message(
    val emitter: Agent? = null,
    val receiver: Agent,
    val date: Long = Date().time,
    val content: String = "",
    val read: Boolean = false
) {
}