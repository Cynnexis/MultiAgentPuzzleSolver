package org.polytech.sma.tp1

class Stopwatch(startNow: Boolean = true) {
	
	var begin: Long = -1
	var end: Long = -1
	
	init {
		begin = if (startNow) System.currentTimeMillis() else -1
		end = -1
	}
	
	fun start() {
		begin = System.currentTimeMillis()
		end = -1
	}
	
	fun stop(): Long {
		if (begin >= 0)
			end = System.currentTimeMillis()
		return elapsed()
	}
	
	fun elapsed(): Long {
		return if (begin >= 0 && end >= 0)
			end - begin
		else
			0
	}
	
	//region OVERRIDES
	
	operator fun invoke(): Long {
		if (begin >= 0 && end == -1L)
			stop()
		else
			start()
		return elapsed()
	}
	
	override fun toString(): String {
		return elapsed().toString()
	}
	
	//endregion
}