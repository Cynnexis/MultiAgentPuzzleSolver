package org.polytech.sma.tp1

import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

fun main() {
	val grid = Grid(5, 5)
	grid.createAgent()
	grid.createAgent()
	grid.createAgent()
	grid.createAgent()
	println(grid.toString())
	Log.init(grid)
	
	grid.startAgents()
	Timer().schedule(10000) {
		grid.stopAgents()
		Timer().schedule(1000) {
			exitProcess(0)
		}
	}
}