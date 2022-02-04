package com.hcardoso.squidbot.models

data class NftAttribute(
    val key: String,
    val value: String,
)

data class NftMetadata(
    val attributes: ArrayList<NftAttribute>,
    val image: String,
    val name: String
)

data class Nft(
    val metadata: NftMetadata
)

data class Player(
    val currency: String,
    val nft_id: Int,
    val nft_contract: String,
    val usdPrice: Float,
    val price: String,
    val nft: Nft,
) {
    fun getSquidEnergy(): Int {
        val se = nft.metadata.attributes.find { it.key == "SquidEnergy" }?.value
        val energy = se?.split("/")?.toTypedArray()?.get(0)

        return energy?.toInt() ?: 0
    }
}