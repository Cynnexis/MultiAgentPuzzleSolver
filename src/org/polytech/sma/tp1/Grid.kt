package org.polytech.sma.tp1

import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class Grid(
	val width: Int = 5,
	val height: Int = 5
) : Observer, Iterable<Agent> {
	
	companion object {
		const val MAX_AGENT = 100
	}
	
	private val agents: ArrayList<Agent> = ArrayList()
	
	override fun update(o: Observable?, arg: Any?) {
		if (o != null && o is Agent)
			this.print()
	}
	
	fun startAgents() {
		for (agent in agents)
			agent.start()
	}
	
	fun stopAgents() {
		for (agent in agents)
			agent.stop()
	}
	
	/**
	 * Create and add an agent in the grid.
	 */
	fun createAgent(): Agent {
		var id = agents.size
		for (a in agents)
			if (a.id >= id)
				id = a.id + 1
		
		var iter = 0
		val MAX_ITER = width * height * 10
		
		var symbol = SymbolsManager.getRandomSymbol()
		while (agents.map { a -> a.symbol }.contains(symbol)) {
			symbol = SymbolsManager.getRandomSymbol()
			if (++iter >= MAX_ITER)
				throw IllegalStateException("Cannot find a unique symbol.")
		}
		
		var position = Pair(Random.nextInt(width), Random.nextInt(height))
		iter = 0
		while (agents.map { a -> a.position }.contains(position)) {
			position = Pair(Random.nextInt(width), Random.nextInt(height))
			if (++iter >= MAX_ITER)
				throw IllegalStateException("Cannot find a unique position.")
		}
		
		var positionFinal = Pair(Random.nextInt(width), Random.nextInt(height))
		iter = 0
		while (agents.map { a -> a.positionFinal }.contains(positionFinal) || positionFinal == position) {
			positionFinal = Pair(Random.nextInt(width), Random.nextInt(height))
			if (++iter >= MAX_ITER)
				throw IllegalStateException("Cannot find a unique final position.")
		}
		
		val agent = Agent(
			this,
			id,
			symbol,
			position,
			position,
			positionFinal
		)
		
		addAgent(agent)
		return agent
	}
	
	fun addAgent(agent: Agent): Boolean {
		// Check that the agent is valid
		
		// Is the ID unique?
		if (agents.map { a -> a.id }.contains(agent.id))
			return false
		
		// Is the symbol unique?
		if (agents.map { a -> a.symbol }.contains(agent.symbol))
			return false
		
		// Is the position unique?
		if (agents.map { a -> a.position }.contains(agent.position))
			return false
		
		// Is the final position unique?
		if (agents.map { a -> a.positionFinal }.contains(agent.positionFinal))
			return false
		
		agent.addObserver(this)
		agents.add(agent)
		return true
	}
	
	fun canMoveAgent(agent: Agent, x: Int, y: Int): Boolean {
		if (x < 0 || width <= x || (agent.position.first - 1 != x && agent.position.first != x && agent.position.first + 1 != x) ||
			y < 0 || height <= y || (agent.position.second - 1 != y && agent.position.second != y && agent.position.second + 1 != y) ||
			agents.map { a -> a.position }.contains(Pair(x, y)))
			return false
		return true
	}
	
	fun moveAgent(agent: Agent, x: Int, y: Int): Boolean {
		if (!canMoveAgent(agent, x, y))
			return false
		agent.position = Pair(x, y)
		return true
	}
	fun moveAgent(agent: Agent, newPosition: Pair<Int, Int>): Boolean {
		return moveAgent(agent, newPosition.first, newPosition.second)
	}
	fun moveAgent(agent: Agent, movement: Movement): Boolean {
		return moveAgent(agent, agent.position.first + movement.xMovement, agent.position.second + movement.yMovement)
	}
	
	fun distanceManhattan(x1: Int, y1: Int, x2: Int, y2: Int): Int {
		return abs(x1 - x2) + abs(y1 - y2)
	}
	fun distanceManhattan(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Int {
		return distanceManhattan(pos1.first, pos1.second, pos2.first, pos2.second)
	}
	
	fun distanceEuclidean(x1: Int, y1: Int, x2: Int, y2: Int): Float {
		return sqrt((x1 - x2).toFloat().pow(2) + (y1 - y2).toFloat().pow(2))
	}
	fun distanceEuclidean(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Float {
		return distanceEuclidean(pos1.first, pos1.second, pos2.first, pos2.second)
	}
	
	fun isFinished(): Boolean {
		for (a in agents)
			if (a.position != a.positionFinal)
				return false
		return true
	}
	
	@Synchronized
	fun print() {
		println(this)
	}
	
	fun getAgentAtPos(x: Int, y: Int): Agent? {
		if (x < 0 || width <= x ||
			y < 0 || height <= y)
			throw IndexOutOfBoundsException("($x, $y) is not valid (width=$width, height=$height)")
		
		for (agent in agents)
			if (agent.position.first == x && agent.position.second == y)
				return agent
		
		return null
	}
	
	fun getAgent(index: Int): Agent {
		return agents[index]
	}
	
	operator fun get(index: Int): Agent {
		return getAgent(index)
	}
	operator fun get(x: Int, y: Int): Agent? {
		return getAgentAtPos(x, y)
	}
	
	override fun iterator(): Iterator<Agent> {
		return agents.iterator()
	}
	
	//region OVERRIDES
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Grid) return false
		
		if (width != other.width) return false
		if (height != other.height) return false
		if (agents != other.agents) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = width
		result = 31 * result + height
		result = 31 * result + agents.hashCode()
		return result
	}
	
	override fun toString(): String {
		val content = StringBuilder()
		for (y in 0 until width) {
			for (x in 0 until height) {
				content.append('|')
					.append(getAgentAtPos(x, y)?.symbol ?: "  ")
				if (x+1 == height)
					content.append('|')
			}
			content.append('\n')
		}
		
		return content.toString()
	}
	
	//endregion
}