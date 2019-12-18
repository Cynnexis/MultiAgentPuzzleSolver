package org.polytech.sma.tp1

import org.junit.jupiter.api.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class PuzzleTest {
	
	companion object {
		const val WIDTH = 5
		const val HEIGHT = 5
		const val NUM_AGENTS = 5
		const val NUM_TRIALS = 100
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
	
	private fun computeSuccess(width: Int = WIDTH, height: Int = HEIGHT, numAgents: Int = NUM_AGENTS, numTrials: Int = NUM_TRIALS, timeoutMs: Long = TIMEOUT_MS, onEpochFinished: (epoch: Int, grid: Grid, success: Int) -> Unit = { _, _, _ -> }): Double {
		var success = 0
		for (epoch in 1..numTrials) {
			val grid = Grid(width, height)
			for (i in 1..numAgents)
				grid.createAgent()
			
			grid.startAgents()
			Thread.sleep(timeoutMs)
			grid.stopAgents()
			success += grid.numberOfWinner()
			onEpochFinished(epoch, grid, success)
		}
		return success.toDouble() / (numTrials * numAgents).toDouble()
	}
	
	@Test
	@Order(1)
	fun success() {
		println("Grid ${WIDTH}x${HEIGHT}, $NUM_TRIALS trial${if (NUM_TRIALS > 1) "s" else ""}, $NUM_AGENTS agent${if (NUM_AGENTS > 1) "s" else ""}, timeout = ${TIMEOUT_MS}ms")
		val success = computeSuccess { epoch: Int, grid: Grid, success: Int ->
			println("Epoch $epoch/$NUM_TRIALS: winners = ${grid.numberOfWinner()}/$NUM_AGENTS (total: $success)")
		}
		println("Success = ${success * 100}% ($NUM_TRIALS trial${if (NUM_TRIALS > 1) "s" else ""}, $NUM_AGENTS agent${if (NUM_AGENTS > 1) "s" else ""})")
	}
	
	@Test
	@Order(2)
	fun testAllNumberOfAgents() {
		val numAgentsLimit = WIDTH * HEIGHT - 1
		val filename = "numAgentsSuccess.csv"
		val file = File(filename)
		file.delete()
		file.writeText("\"Testing all number of agents, from 1 tp $numAgentsLimit\",\"" + SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date()) + "\"" + System.lineSeparator())
		file.appendText("\"Number of agents\",\"Success\",\"Time (ms)\"" + System.lineSeparator())
		val timer = Stopwatch(false)
		for (numAgents in 1..24) {
			timer.start()
			val success = computeSuccess(numAgents = numAgents)
			timer.stop()
			println("Success for $numAgents agent${if (NUM_AGENTS > 1) "s" else ""} = ${success * 100}% (elapsed ${timer.elapsed()}ms)")
			file.appendText("\"$numAgents\",\"$success\",\"${timer.elapsed()}\"" + System.lineSeparator())
		}
	}
}