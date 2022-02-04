package com.hcardoso.squidbot

import com.hcardoso.squidbot.models.*
import com.hcardoso.squidbot.services.BiswapMarketService
import com.hcardoso.squidbot.services.TelegramBotService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@SpringBootApplication
class SquidbotApplication {

	@Autowired lateinit var biswapMarketService: BiswapMarketService
	@Autowired lateinit var telegramBotService: TelegramBotService

	val filter = PlayerFilter(
		energy = 900,
		maxPrice = 60,
	)
	val notifiedPlayersId: ArrayList<Int> = ArrayList()

	fun filterPlayers(players: ArrayList<Player>): ArrayList<Player> {
		val validPlayers: ArrayList<Player> = ArrayList()

		players.forEach {
			if (it.getSquidEnergy() >= filter.energy) {
				validPlayers.add(it)
			}
		}

		return validPlayers
	}

	fun createPhotoCaption(player: Player): String {
		val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
		format.maximumFractionDigits = 0;

		var caption = ""

		caption += "<b>NAME:</b> ${player.nft.metadata.name}\n"
		caption += "<b>PRICE:</b> ${format.format(player.usdPrice)}\n"
		caption += "<b>CURRENCY:</b> ${player.currency}\n"
		caption += "<b>SE:</b> ${player.nft.metadata.attributes.find { it.key == "SquidEnergy" }?.value}"

		return caption
	}

	suspend fun searchPlayers() {
		val players = biswapMarketService.searchPlayers(
			PlayerSearchFilter(
				usdRange = UsdRange(filter.minPrice, filter.maxPrice)
			)
		)
		val filteredPlayers = filterPlayers(players)

		filteredPlayers.forEach { player ->
			val buttons: ArrayList<TelegramButton> = ArrayList()
			buttons.add(
				TelegramButton(
					text = "Buy",
					url = "https://marketplace.biswap.org/card/${player.nft_contract}/${ player.nft_id }"
				)
			)

			val hasNotified = notifiedPlayersId.find { it == player.nft_id }

			if (hasNotified == null) {
				telegramBotService.sendPhoto(
					caption = createPhotoCaption(player),
					photoUrl = player.nft.metadata.image,
					buttons = TelegramButtons(buttons)
				)
				notifiedPlayersId.add(player.nft_id)
				println("Notified player ${ player.nft.metadata.name }")
			}
		}
	}

	@Bean
	fun init() {
		println("Start search")
		while (true) {
			runBlocking {
				launch {
					searchPlayers()
					delay(30000)
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<SquidbotApplication>(*args)
}
