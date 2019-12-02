package org.polytech.sma.tp1

import kotlin.random.Random

object SymbolsManager : Iterable<String> {
	
	val symbols = arrayListOf(
		"❤",
		"\uD83D\uDE00",
		"\uD83D\uDE3A",
		"\uD83D\uDC35",
		"\uD83E\uDD81",
		"\uD83D\uDC3A",
		"\uD83D\uDC2F",
		"\uD83E\uDD92",
		"\uD83E\uDD8A",
		"\uD83D\uDC3B",
		"\uD83D\uDC30",
		"\uD83D\uDC39",
		"\uD83D\uDC2D",
		"\uD83D\uDC17",
		"\uD83D\uDC37",
		"\uD83D\uDC2E",
		"\uD83D\uDC36"
		)
	
	fun getRandomSymbol(): String {
		return symbols[Random.nextInt(symbols.size)]
	}
	
	operator fun get(index: Int): String {
		return symbols[index]
	}
	
	override fun iterator(): Iterator<String> {
		return symbols.iterator()
	}
}