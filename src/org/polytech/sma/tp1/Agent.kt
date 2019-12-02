package org.polytech.sma.tp1

import sun.management.resources.agent
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.random.Random

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
        /**
         * TODO do:
         * while (!puzzle_finished()) {
         *  communicate()
         *  resonate()
         *  decide()
         *  apply()
         * }
         */
        threadBool.set(true)
	    while (position != positionFinal && threadBool.get()) {
		    var movement = Movement.STAY
		    
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
			    break
		    // If the goal is farther away horizontally than vertically
		    else if (deltaX >= deltaY) {
			    // If the goal is on the left
			    if (deltaX > 0)
				    movement = Movement.LEFT
			    // If the goal is on the right
			    else
				    movement = Movement.LEFT
		    }
		    // If the goal is farther away vertically than horizontally
		    else {
			    // If the goal is up
			    if (deltaY > 0)
				    movement = Movement.UP
			    // If the goal is down
			    else
				    movement = Movement.DOWN
		    }
		    
		    // Check if we can move
		    if (!grid.canMoveAgent(this, movement)) {
			    // Otherwise, turn to avoid obstacles
			    if (grid.canMoveAgent(this, movement.turnClockwise()))
				    movement = movement.turnClockwise()
			    else
				    movement = movement.turnAnticlockwise()
		    }
		    
			grid.moveAgent(this, movement)
	    }
	
	    print("$_id ($_symbol) ")
	    if (position == positionFinal)
		    print("has reached its destination!")
	    else
		    print("could not reached its destination...")
    }
}