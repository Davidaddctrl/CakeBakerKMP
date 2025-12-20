package com.davidlukash.cakebaker.data

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

data class Upgrade(
    val pageName: String,
    val price: Int,
    val priceGrowth: UpgradeGrowth,
    val imageName: String,
    val maxLevel: Int?,
    val buyFunction: ((Int, JsonObject) -> Unit),
    val options: JsonObject = buildJsonObject {  },
    val counter: Int = 0,
)
