package com.hcardoso.squidbot.models

data class PlayerSearchFilter(
    val currencies: ArrayList<String> = ArrayList(),
    val levels: ArrayList<String> = ArrayList(),
    val robiBoost: RobiBoost = RobiBoost(0, 0),
    val onlyBoosted: Boolean = false,
    val usdRange: UsdRange
)

data class RobiBoost(
    val from: Int,
    val to: Int
)

data class UsdRange(
    val from: Int,
    val to: Int
)