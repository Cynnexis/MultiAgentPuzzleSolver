package org.polytech.sma.tp1

import com.google.gson.*
import java.net.URL
import kotlin.random.Random

object SymbolsManager : Iterable<String> {
	
	var symbols = ArrayList<String>()
	
	init {
		// Download emoji list
		val url = URL("https://unpkg.com/emoji.json@12.1.0/emoji.json")
		val jsonText = url.readText()
		val root: JsonArray = JsonParser.parseString(jsonText).asJsonArray
		val set = HashSet<String>()
		for (emoji in root)
			set.add(emoji.asJsonObject.get("char").asString.first().toString())
		symbols = ArrayList(set)
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