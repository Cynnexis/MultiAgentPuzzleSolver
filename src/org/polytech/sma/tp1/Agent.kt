package org.polytech.sma.tp1

import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

data class Agent(
    private val _id: Int = Random.nextInt(),
    private val _symbol: String = SymbolsManager.getRandomSymbol(),
    @Volatile
    private var _position: Pair<Int, Int> = Pair(0, 0),
    private val _positionInit: Pair<Int, Int> = Pair(0, 0),
    private val _positionFinal: Pair<Int, Int> = Pair(0, 0)
) : Observable(), Runnable {
    
    private var thread: Thread? = null
    
    val id: Int
        get() = _id
    
    val symbol: String
        get() = _symbol

    var position: Pair<Int, Int>
        get() = _position
        set(value) {
            _position = value
            notifyObservers(_position)
            setChanged()
        }

    val positionInit: Pair<Int, Int>
        get() = _positionInit

    val positionFinal: Pair<Int, Int>
        get() = _positionFinal
    
    fun start() {
        if (thread == null) {
	        thread = thread {
		        run()
	        }
        }
    }

    override fun run() {
        println("Hey! I'm $this");
        /**
         * TODO do:
         * while (!puzzle_finished()) {
         *  communicate()
         *  resonate()
         *  decide()
         *  apply()
         * }
         */
    }
}