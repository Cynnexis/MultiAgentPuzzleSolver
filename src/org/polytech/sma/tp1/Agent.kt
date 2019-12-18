package org.polytech.sma.tp1

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.math.abs

data class Agent(
	private val _grid: Grid,
	private val _id: Int = Random.nextInt(),
	private val _symbol: String = SymbolsManager.getRandomSymbol(),
	@Volatile
	private var _position: Pair<Int, Int> = Pair(0, 0),
	private val _positionInit: Pair<Int, Int> = Pair(0, 0),
	private val _positionFinal: Pair<Int, Int> = Pair(0, 0)
) : Observable(), Runnable {
	
	private var thread: Thread? = null
	private var threadBool: AtomicBoolean = AtomicBoolean(true)
 
	val grid: Grid
		get() = _grid
	
	val id: Int
		get() = _id
	
	val symbol: String
		get() = _symbol

	var position: Pair<Int, Int>
		get() = _position
		set(value) {
			_position = value
			setChanged()
			notifyObservers(_position)
		}

	val positionInit: Pair<Int, Int>
		get() = _positionInit

	val positionFinal: Pair<Int, Int>
		get() = _positionFinal
	
	fun start() {
		if (thread == null || thread?.isAlive == true) {
			thread = thread {
				run()
			}
		}
	}
	fun stop() {
		threadBool.set(false)
		thread?.join(500)
	}

	override fun run() {
		threadBool.set(true)
		var iter = 0
		while (!grid.isFinished() && threadBool.get()) {
			var movement = Movement.STAY
			
			var mail = Mailbox(this)
			
			// If we have a mail, execute it, otherwise, try to go to the goal
			if (mail != null) {
				if (grid.canMoveAgent(this, mail.movementWhereEmitterIs.turnClockwise()))
					movement = mail.movementWhereEmitterIs.turnClockwise()
				else if (grid.canMoveAgent(this, mail.movementWhereEmitterIs.turnAnticlockwise()))
					movement = mail.movementWhereEmitterIs.turnAnticlockwise()
				else if (grid.canMoveAgent(this, mail.movementWhereEmitterIs.invert()))
					movement = mail.movementWhereEmitterIs.invert()
				else
					// Else ignore and go to the next condition to go to goal
					mail = null
			}
			if (mail == null) {
				val deltaX = position.first - positionFinal.first
				val deltaY = position.second - positionFinal.second
				
				// deltaX > 0 => the goal is on the left
				// deltaX < 0 => the goal is on the right
				// deltaX = 0 => the agent is vertically aligned with the goal
				// deltaY > 0 => the goal is up
				// deltaY < 0 => the goal is down
				// deltaY = 0 => the agent is horizontally aligned with the goal
				
				// If the goal is achieved
				if (deltaX == 0 && deltaY == 0)
					movement = Movement.STAY
				// If the goal is farther away horizontally than vertically
				else if (abs(deltaX) >= abs(deltaY)) {
					// If the goal is on the left
					movement = if (deltaX > 0)
						Movement.LEFT
					// If the goal is on the right
					else
						Movement.RIGHT
				}
				// If the goal is farther away vertically than horizontally
				else {
					// If the goal is up
					movement = if (deltaY > 0)
						Movement.UP
					// If the goal is down
					else
						Movement.DOWN
				}
				
				// Check if we can move
				if (!grid.canMoveAgent(this, movement)) {
					// Otherwise, send a mail to the obstacle (agent)
					movement = try {
						Mailbox.send(Message(
							this,
							_grid[_position.first + movement.xMovement, _position.second + movement.yMovement]!!,
							movement.invert()
						))
						Movement.STAY
					} catch (e: Exception) {
						// Otherwise, turn to avoid obstacles
						if (grid.canMoveAgent(this, movement.turnClockwise()))
							movement.turnClockwise()
						else
							movement.turnAnticlockwise()
					}
				}
			}
			
			grid.moveAgent(this, movement)
			Log.log(this)
			iter++
		}
		
		synchronized(this) {
			if (Log.ENABLE_LOGGING) {
				print("$_id ($_symbol) ")
				if (position == positionFinal)
					print("has reached its destination $_positionFinal !")
				else
					print("could not reached its destination $_positionFinal... (distance from goal = {euclidean=${_grid.distanceEuclidean(_position, _positionFinal)}, manhattan=${_grid.distanceManhattan(_position, _positionFinal)} })")
				println("\t(nb iter = $iter)")
			}
		}
	}
}