package org.polytech.sma.tp1

enum class Movement(
	val xMovement: Int,
	val yMovement: Int
) {
	STAY(0, 0),
	UP(0, -1),
	DOWN(0, 1),
	LEFT(-1, 0),
	RIGHT(1, 0);
	
	fun toPair(): Pair<Int, Int> = Pair(xMovement, yMovement)
	
	fun invert(): Movement {
		return when (this) {
			STAY -> STAY
			UP -> DOWN
			DOWN -> UP
			LEFT -> RIGHT
			RIGHT -> LEFT
		}
	}
	
	fun turnClockwise(): Movement {
		return when (this) {
			STAY -> STAY
			UP -> RIGHT
			DOWN -> LEFT
			LEFT -> UP
			RIGHT -> DOWN
		}
	}
	fun turnAnticlockwise(): Movement = turnClockwise().invert()
	
	operator fun plus(movement: Movement): Pair<Int, Int> {
		return plus(Pair(movement.xMovement, movement.yMovement))
	}
	operator fun plus(movement: Pair<Int, Int>): Pair<Int, Int> {
		return Pair(xMovement + movement.first, yMovement + movement.second)
	}
	
	operator fun minus(movement: Movement): Pair<Int, Int> {
		return minus(Pair(movement.xMovement, movement.yMovement))
	}
	operator fun minus(movement: Pair<Int, Int>): Pair<Int, Int> {
		return Pair(xMovement - movement.first, yMovement - movement.second)
	}
	
	operator fun times(movement: Movement): Pair<Int, Int> {
		return times(Pair(movement.xMovement, movement.yMovement))
	}
	operator fun times(movement: Pair<Int, Int>): Pair<Int, Int> {
		return Pair(xMovement * movement.first, yMovement * movement.second)
	}
	
	operator fun div(movement: Movement): Pair<Int, Int> {
		return div(Pair(movement.xMovement, movement.yMovement))
	}
	operator fun div(movement: Pair<Int, Int>): Pair<Int, Int> {
		return Pair(xMovement / movement.first, yMovement / movement.second)
	}
	
	operator fun not(): Movement = invert()
}

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> {
	return Pair(first + pair.first, second + pair.second)
}
operator fun Pair<Int, Int>.minus(pair: Pair<Int, Int>): Pair<Int, Int> {
	return Pair(first - pair.first, second - pair.second)
}
operator fun Pair<Int, Int>.times(pair: Pair<Int, Int>): Pair<Int, Int> {
	return Pair(first * pair.first, second * pair.second)
}
operator fun Pair<Int, Int>.div(pair: Pair<Int, Int>): Pair<Int, Int> {
	return Pair(first / pair.first, second / pair.second)
}