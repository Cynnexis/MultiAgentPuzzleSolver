package org.polytech.sma.tp1

import java.io.File
import kotlin.concurrent.thread

object Log {
	
	private var isInitialized = false
	const val GRID_FILENAME = "grid.log"
	
	var ENABLE_LOGGING = true
	
	@Synchronized
	fun init(grid: Grid) {
		if (!isInitialized && ENABLE_LOGGING) {
			File(GRID_FILENAME).delete()
			File(GRID_FILENAME).writeText("Grid ${grid.width}x${grid.height} with ${grid.size()} agent${if (grid.size() > 0) "s" else ""}\n\n")
			
			for (agent in grid) {
				val filename = getAgentFilename(agent)
				File(filename).delete()
				File(filename).writeText("Agent ${agent.id} (${agent.symbol})\n\n")
			}
			isInitialized = true
		}
	}
	
	@Synchronized
	fun log(grid: Grid) {
		if (ENABLE_LOGGING) {
			if (!isInitialized)
				init(grid)
			File(GRID_FILENAME).appendText(grid.toString() + "\n")
		}
	}
	
	@Synchronized
	fun log(agent: Agent) {
		if (ENABLE_LOGGING) {
			if (!isInitialized)
				return
			
			File(getAgentFilename(agent)).appendText("position=${agent.position}\n")
		}
	}
	
	private fun getAgentFilename(agent: Agent): String {
		return "agent${agent.id}.log"
	}
}