package com.hcardoso.squidbot.models

class TelegramButtonsReplyMarkup(
    val inline_keyboard: ArrayList<ArrayList<TelegramButton>> = ArrayList()
)

class TelegramButtons(
    private val buttons: ArrayList<TelegramButton> = ArrayList()
) {
    fun createReplyMarkup() = TelegramButtonsReplyMarkup(
        inline_keyboard = arrayListOf(buttons)
    )
}