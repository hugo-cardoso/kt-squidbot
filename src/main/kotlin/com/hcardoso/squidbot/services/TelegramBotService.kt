package com.hcardoso.squidbot.services

import com.google.gson.Gson
import com.hcardoso.squidbot.configs.AppConfig
import com.hcardoso.squidbot.models.TelegramButtons
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TelegramBotService {

    @Autowired lateinit var appConfig: AppConfig

    val client = HttpClient(CIO)
    val gson = Gson()

    suspend fun sendPhoto(
        caption: String,
        photoUrl: String,
        buttons: TelegramButtons,
    ) {
        try {
            val response: HttpResponse = client.get("https://api.telegram.org/bot${ appConfig.telegramBotToken }/sendPhoto") {
                parameter("chat_id", appConfig.telegramGroupId)
                parameter("parse_mode", "HTML")
                parameter("caption", caption)
                parameter("photo", photoUrl)
                parameter("reply_markup", gson.toJson(buttons.createReplyMarkup()))
            }
        } catch (e: Exception) {
            println("Error...")
        }
    }
}