package org.polytech.sma.tp1

import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	val grid = Grid(5, 5)
	grid.createAgent()
	grid.createAgent()
	grid.createAgent()
	grid.createAgent()
	println(grid.toString())
	grid.startAgents()
	Timer().schedule(5000) {
		grid.stopAgents()
		Timer().schedule(1000) {
			exitProcess(0)
		}
	}
}