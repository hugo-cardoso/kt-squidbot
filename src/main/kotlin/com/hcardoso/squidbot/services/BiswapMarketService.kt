package com.hcardoso.squidbot.services

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import com.hcardoso.squidbot.models.Player
import com.hcardoso.squidbot.models.PlayerSearchFilter
import models.PlayerSearchFilterBody
import models.PlayerSearchResponse
import org.springframework.stereotype.Service

@Service
class BiswapMarketService {
    val client = HttpClient(CIO)
    val gson = Gson()

    suspend fun searchPlayers(filter: PlayerSearchFilter? = null): ArrayList<Player> {
        return try {
            val jsonFilter = gson.toJson(PlayerSearchFilterBody(filter))
            val response: HttpResponse = client.post("https://marketplace.biswap.org/back/offers/sellings") {
                parameter("partner", "61be229e6b84d59feeb0366c")
                parameter("sortBy", "newest")
                parameter("userAddress", "0xfa1ca08E9A822566e8891E53353b1821EC57Ebff")
                parameter("page", "0")
                contentType(ContentType.Application.Json)
                body = jsonFilter
            }
            gson.fromJson(response.readText(), PlayerSearchResponse::class.java).data
        } catch (e: Exception) {
            println(e)
            throw Exception()
        }

    }
}