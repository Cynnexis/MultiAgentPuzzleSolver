package org.polytech.sma.tp1

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PuzzleTest {
	
	companion object {
		const val WIDTH = 5
		const val HEIGHT = 5
		const val NUM_AGENTS = 4
		const val NUM_TRIALS = 10
		const val TIMEOUT_MS: Long = 100
	}
	
	private val timer = Stopwatch()
	
	@BeforeEach
	fun setUp() {
		Log.ENABLE_LOGGING = false
		timer.start()
	}
	
	@AfterEach
	fun tearDown() {
		println("Time elapsed: ${timer.stop()}ms")
	}
	
	@Test
	fun accuracy() {
		println("Grid ${WIDTH}x${HEIGHT}, $NUM_TRIALS trial${if (NUM_TRIALS > 1) "s" else ""}, $NUM_AGENTS agent${if (NUM_AGENTS > 1) "s" else ""}, timeout = ${TIMEOUT_MS}ms")
		var success = 0
		for (epoch in 1..NUM_TRIALS) {
			val grid = Grid(WIDTH, HEIGHT)
			for (i in 1..NUM_AGENTS)
				grid.createAgent()
			
			grid.startAgents()
			Thread.sleep(TIMEOUT_MS)
			grid.stopAgents()
			success += grid.numberOfWinner()
			println("Epoch $epoch/$NUM_TRIALS: winners = ${grid.numberOfWinner()}/$NUM_AGENTS (total: $success)")
		}
		val accuracy: Double = success.toDouble() / (NUM_TRIALS * NUM_AGENTS).toDouble()
		println("Accuracy = ${accuracy * 100}% ($NUM_TRIALS trial${if (NUM_TRIALS > 1) "s" else ""}, $NUM_AGENTS agent${if (NUM_AGENTS > 1) "s" else ""})")
	}
}