package org.polytech.sma.tp1

import com.google.gson.*
import java.net.URL
import kotlin.random.Random

object SymbolsManager : Iterable<String> {
	
	var symbols = ArrayList<String>()
	
	init {
		val url = URL("https://unpkg.com/emoji.json@12.1.0/emoji.json")
		val jsonText = url.readText()
		val root: JsonArray = JsonParser.parseString(jsonText).asJsonArray
		for (emoji in root) {
			val char: String = emoji.asJsonObject.get("char").asString.first().toString()
			if (!symbols.contains(char))
				symbols.add(char)
		}
	}
	
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