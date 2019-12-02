package org.polytech.sma.tp1

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
    
    var thread: Thread? = null
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
	    var lastResult = true
	    var lastResultCounter = 0
	    while (position != positionFinal && lastResultCounter < 50 && threadBool.get()) {
			val result = grid.moveAgent(this, Movement.RIGHT)
		    if (result != lastResult) {
			    lastResult = result
			    lastResultCounter = 0
		    }
		    else
			    lastResultCounter++
	    }
    }
}