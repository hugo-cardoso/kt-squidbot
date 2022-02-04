package models

import com.hcardoso.squidbot.models.Player

data class PlayerSearchResponse(
    val counter: Int,
    val data: ArrayList<Player>
)