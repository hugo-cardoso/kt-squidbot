package com.hcardoso.squidbot.configs

import io.github.cdimascio.dotenv.dotenv
import org.springframework.stereotype.Component

@Component
class AppConfig {
    private final val dotenv = dotenv()

    val telegramBotToken = dotenv.get("TELEGRAM_BOTTOKEN")
    val telegramGroupId = dotenv.get("TELEGRAM_GROUPID")
}