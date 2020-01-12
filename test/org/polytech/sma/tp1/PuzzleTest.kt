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
		const val NUM_TRIALS = 1000
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
	
	/**
	 * Compute the individual and collective success for a series of simulation. The individual success is a percentage
	 * of agents that successively reached their goal, divided by the number of trials times the number of agents. The
	 * collective number is the percentage of simulation that succeeded, divided by the number of trial.
	 * @param width The width of the grid. Default is WIDTH.
	 * @param height The height of the grid. Default is HEIGHT.
	 * @param numAgents The number of agents in the simulation. Default is NUM_AGENTS.
	 * @param numTrials The number of trials. Default is NUM_TRIALS.
	 * @param timeoutMs The timeout in milliseconds before the simulation stops. Default is TIMEOUT_MS.
	 * @param onEpochFinished Callback that is called at the end of each epoch. Default is empty callback.
	 * @return Return a pair of individual success and collective success.
	 */
	private fun computeSuccess(
		width: Int = WIDTH,
		height: Int = HEIGHT,
		numAgents: Int = NUM_AGENTS,
		numTrials: Int = NUM_TRIALS,
		timeoutMs: Long = TIMEOUT_MS,
		onEpochFinished: (epoch: Int, grid: Grid, individualSuccess: Int, collectiveSuccess: Int) -> Unit = { _, _, _, _ -> }): Pair<Double, Double> {
		var individualSuccess = 0
		var collectiveSuccess = 0
		for (epoch in 1..numTrials) {
			val grid = Grid(width, height)
			for (i in 1..numAgents)
				grid.createAgent()
			
			grid.startAgents()
			Thread.sleep(timeoutMs)
			grid.stopAgents()
			individualSuccess += grid.numberOfWinner()
			collectiveSuccess += if (grid.isFinished()) 1 else 0
			onEpochFinished(epoch, grid, individualSuccess, collectiveSuccess)
		}
		return Pair(individualSuccess.toDouble() / (numTrials * numAgents).toDouble(), collectiveSuccess.toDouble() / numTrials.toDouble())
	}
	
	@Test
	@Order(1)
	fun success() {
		println("Grid ${WIDTH}x${HEIGHT}, $NUM_TRIALS trial${if (NUM_TRIALS > 1) "s" else ""}, $NUM_AGENTS agent${if (NUM_AGENTS > 1) "s" else ""}, timeout = ${TIMEOUT_MS}ms")
		val success = computeSuccess { epoch: Int, grid: Grid, individualSuccess: Int, collectiveSuccess: Int ->
			println("Epoch $epoch/$NUM_TRIALS: winners = ${grid.numberOfWinner()}/$NUM_AGENTS (individual: $individualSuccess, collective: $collectiveSuccess)")
		}
		val individualSuccess = success.first
		val collectiveSuccess = success.second
		println("Individual success = ${individualSuccess * 100}%, collective = ${collectiveSuccess * 100}% ($NUM_TRIALS trial${if (NUM_TRIALS > 1) "s" else ""}, $NUM_AGENTS agent${if (NUM_AGENTS > 1) "s" else ""})")
	}
	
	@Test
	@Order(2)
	fun testAllNumberOfAgents() {
		val numAgentsLimit = WIDTH * HEIGHT - 1
		val filename = "numAgentsSuccess.csv"
		val file = File(filename)
		file.delete()
		file.writeText("\"Testing all number of agents, from 1 tp $numAgentsLimit\",\"" + SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date()) + "\"" + System.lineSeparator())
		file.appendText("\"Number of agents\",\"Individual Success\",\"Collective Success\",\"Time (ms)\"" + System.lineSeparator())
		val timer = Stopwatch(false)
		for (numAgents in 1..24) {
			timer.start()
			val success = computeSuccess(numAgents = numAgents)
			val individualSuccess = success.first
			val collectiveSuccess = success.second
			timer.stop()
			println("Success for $numAgents agent${if (NUM_AGENTS > 1) "s" else ""} = individual:${individualSuccess * 100}%, collective:${collectiveSuccess * 100} (elapsed ${timer.elapsed()}ms)")
			file.appendText("\"$numAgents\",\"$individualSuccess\",\"$collectiveSuccess\",\"${timer.elapsed()}\"" + System.lineSeparator())
		}
	}
}