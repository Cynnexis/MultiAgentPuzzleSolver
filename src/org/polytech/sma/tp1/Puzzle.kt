package org.polytech.sma.tp1

fun main(args: Array<String>) {
	val grid = Grid(5, 5)
	grid.createAgent()
	grid.createAgent()
	grid.createAgent()
	grid.createAgent()
	println(grid.toString())
}