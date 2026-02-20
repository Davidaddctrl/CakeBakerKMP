package com.davidlukash.cakebaker.data.save

import cakebaker.composeapp.generated.resources.Res
import com.davidlukash.cakebaker.ForMigrationSupport
import com.davidlukash.cakebaker.JsonMathHelpers
import com.davidlukash.cakebaker.VERSION
import com.davidlukash.cakebaker.VERSIONCODE
import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.item.ItemType
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.order.OrderCakeSettings
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.json
import com.davidlukash.jsonmath.createObject
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.serialization.Serializable
import kotlin.collections.plus

const val GENERATE_UPGRADE_LOCALISATIONS = false

@Serializable
data class Save(
    val version: String = "Beta 0.9.1",
    val versionCode: Int? = 0,
    val items: List<Item> = listOf(),
    val currentCakeTier: Int = 1,
    val upgrades: List<Upgrade> = listOf(),
    val ovenProgress: Double = 0.0,
    val ovenRunning: Boolean = false,
    @ForMigrationSupport
    val autoOvenEnabled: Boolean? = null,
    @ForMigrationSupport
    val autoOrderCompleteEnabled: Boolean? = null,
    val tempCakeTier: Int = 1,
    val customerSatisfaction: Int = 1,
    val orderCakeSettings: Map<Int, OrderCakeSettings> = mapOf(),
    val orders: List<Order> = listOf(),
    val orderCakeTimeCounters: Map<Int, Double> = mapOf(),
    val isFirstBake: Boolean = false,
) {
    @OptIn(ForMigrationSupport::class)
    fun forcedMigration(): Save {
        var save = this

        save = save.copy(
            upgrades = save.upgrades.map {
                it.copy(
                    id = if (it.id == "upgrade.missing") {
                        when (it.name) {
                            "Auto Oven" -> "upgrade.auto_oven"
                            "Auto Order Complete" -> "upgrade.auto_order_complete"
                            else -> it.name
                        }
                    } else it.id
                )
            },
            items = save.items.map {
                it.copy(
                    id = if (it.id == "item.missing") {
                        if (it.name == "Money") "item.money" else it.name
                    } else it.id,
                    image = if (it.image == "image.missing") it.name else it.image,
                )
            }
        )

        val autoOven = save.upgrades.find { it.id == "upgrade.auto_oven" }
        if (autoOven != null) {
            if (save.autoOvenEnabled != null) {
                save = save.copy(
                    upgrades = save.upgrades.map {
                        if (it.id == "upgrade.auto_oven") {
                            autoOven.copy(
                                parameters = autoOven.parameters + mapOf(
                                    "enabled" to createObject(save.autoOvenEnabled)
                                )
                            )
                        } else it
                    }
                )
            } else if (autoOven.parameters["enabled"]?.asBoolean() == null) {
                save = save.copy(
                    upgrades = save.upgrades.map {
                        if (it.id == "upgrade.auto_oven") {
                            autoOven.copy(
                                parameters = autoOven.parameters + mapOf(
                                    "enabled" to createObject(false)
                                )
                            )
                        } else it
                    }
                )
            }
        }

        val autoOrderComplete = save.upgrades.find { it.id == "upgrade.auto_order_complete" }
        if (autoOrderComplete != null) {
            if (save.autoOrderCompleteEnabled != null) {
                save = save.copy(
                    upgrades = save.upgrades.map {
                        if (it.id == "upgrade.auto_order_complete") {
                            autoOrderComplete.copy(
                                parameters = autoOrderComplete.parameters + mapOf(
                                    "enabled" to createObject(save.autoOrderCompleteEnabled)
                                )
                            )
                        } else it
                    }
                )
            } else if (autoOrderComplete.parameters["enabled"]?.asBoolean() == null) {
                save = save.copy(
                    upgrades = save.upgrades.map {
                        if (it.id == "upgrade.auto_order_complete") {
                            autoOrderComplete.copy(
                                parameters = autoOrderComplete.parameters + mapOf(
                                    "enabled" to createObject(false)
                                )
                            )
                        } else it
                    }
                )
            }
        }
        return save
    }

    companion object {
        lateinit var version0: Save
        
        suspend fun readOldSaves() {
            version0 = json.decodeFromString<Save>(Res.readBytes("files/save_version_0.json").decodeToString())
                .forcedMigration()
        }

        fun convertUnderscoreToName(name: String) =
            name.split("_").joinToString(" ") { part -> part.replaceFirstChar { it.uppercaseChar() } }

        val default = Save(
            version = VERSION,
            versionCode = VERSIONCODE,
            items = listOf(
                Item(
                    name = "item.butter.name",
                    id = "item.butter",
                    image = "image.butter",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.4f),
                    price = BigDecimal.fromFloat(200f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.1f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0.2f),
                        2 to BigDecimal.fromFloat(0.4f),
                        3 to BigDecimal.fromFloat(0.8f),
                    ),
                ),
                Item(
                    name = "item.egg.name",
                    id = "item.egg",
                    image = "image.egg",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(4f),
                    price = BigDecimal.fromFloat(75f),
                    fastPriceGrowth = false,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.075f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(4f),
                        2 to BigDecimal.fromFloat(8f),
                        3 to BigDecimal.fromFloat(12f),
                    ),
                ),
                Item(
                    name = "item.flour.name",
                    id = "item.flour",
                    image = "image.flour",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.2f),
                    price = BigDecimal.fromFloat(375f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.2f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0.1f),
                        2 to BigDecimal.fromFloat(0.2f),
                        3 to BigDecimal.fromFloat(0.4f),
                    ),
                ),
                Item(
                    name = "item.sugar.name",
                    id = "item.sugar",
                    image = "image.sugar",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.4f),
                    price = BigDecimal.fromFloat(200f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.1f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0.2f),
                        2 to BigDecimal.fromFloat(0.4f),
                        3 to BigDecimal.fromFloat(0.8f),
                    ),
                ),
                Item(
                    name = "item.vanilla_extract.name",
                    id = "item.vanilla_extract",
                    image = "image.vanilla_extract",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.5f),
                    price = BigDecimal.fromFloat(125f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0.5f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.15f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0.5f),
                        2 to BigDecimal.fromFloat(1f),
                        3 to BigDecimal.fromFloat(2f),
                    ),
                ),
                Item(
                    name = "item.baking_powder.name",
                    id = "item.baking_powder",
                    image = "image.baking_powder",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.4f),
                    price = BigDecimal.fromFloat(200f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0.2f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.1f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0.2f),
                        2 to BigDecimal.fromFloat(0.4f),
                        3 to BigDecimal.fromFloat(0.8f),
                    ),
                ),
                Item(
                    name = "item.cocoa_powder.name",
                    id = "item.cocoa_powder",
                    image = "image.cocoa_powder",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0f),
                    price = BigDecimal.fromFloat(5000f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.3f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0.0f),
                        2 to BigDecimal.fromFloat(0.5f),
                        3 to BigDecimal.fromFloat(0.0f),
                    ),
                ),
                Item(
                    name = "item.honey_pot.name",
                    id = "item.honey_pot",
                    image = "image.honey_pot",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0f),
                    price = BigDecimal.fromFloat(15000f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(1f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(0f),
                        2 to BigDecimal.fromFloat(0f),
                        3 to BigDecimal.fromFloat(1f),
                    ),
                ),
                Item(
                    name = "item.vanilla_cake.name",
                    id = "item.vanilla_cake",
                    image = "image.vanilla_cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 1,
                    salePrice = BigDecimal.fromFloat(1350f)
                ),
                Item(
                    name = "item.chocolate_cake.name",
                    id = "item.chocolate_cake",
                    image = "image.chocolate_cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 2,
                    salePrice = BigDecimal.fromFloat(6000f)
                ),
                Item(
                    name = "item.honey_cake.name",
                    id = "item.honey_cake",
                    image = "image.honey_cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 3,
                    salePrice = BigDecimal.fromFloat(6000f)
                ),
                Item(
                    name = "item.money.name",
                    id = "item.money",
                    image = "image.money",
                    type = ItemType.CURRENCY,
                    amount = BigDecimal.fromFloat(0f),
                )
            ),
            currentCakeTier = 1,
            upgrades = listOf(
                //region Cake Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.cake",
                    imageName = "image.vanilla_cake",
                    name = "upgrade.expensive_vanilla_cakes.name",
                    id = "upgrade.expensive_vanilla_cakes",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createLinearGrowthSmall() + JsonMathHelpers.createOperationSmall("math.product"),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(10.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(2.toBigDecimal()),
                        "initialPrice" to createObject(2.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "variable" to createObject("globals.items.item.vanilla_cake.salePrice"),
                        "argument" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.cake",
                    imageName = "image.chocolate_cake",
                    name = "upgrade.expensive_chocolate_cakes.name",
                    id = "upgrade.expensive_chocolate_cakes",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createLinearGrowthSmall() + JsonMathHelpers.createOperationSmall("math.product"),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(15.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(3.toBigDecimal()),
                        "initialPrice" to createObject(5.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "variable" to createObject("globals.items.item.chocolate_cake.salePrice"),
                        "argument" to createObject(1.4.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.cake",
                    imageName = "image.honey_cake",
                    name = "upgrade.expensive_honey_cakes.name",
                    id = "upgrade.expensive_honey_cakes",
                    price = 6,
                    cakeTier = 2,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createLinearGrowthSmall() + JsonMathHelpers.createOperationSmall("math.product"),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(4.toBigDecimal()),
                        "initialPrice" to createObject(6.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "variable" to createObject("globals.items.item.honey_cake.salePrice"),
                        "argument" to createObject(1.7.toBigDecimal()),
                    )
                ),
                //endregion
                //region Oven Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.oven",
                    imageName = "image.oven",
                    name = "upgrade.faster_oven.name",
                    id = "upgrade.faster_oven",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = 45,
                    onBuy = JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(35.toBigDecimal()) to createObject(2.toBigDecimal())
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(15.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.oven",
                    imageName = "image.oven",
                    name = "upgrade.auto_oven.name",
                    id = "upgrade.auto_oven",
                    price = 3,
                    cakeTier = 1,
                    maxLevel = 1,
                    onBuy = JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(mapOf()),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "enabled" to createObject(false),
                    )
                ),
                //endregion
                //region Order Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.orders",
                    imageName = "image.face.happy",
                    name = "upgrade.auto_order_complete.name",
                    id = "upgrade.auto_order_complete",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = 1,
                    onBuy = JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(mapOf()),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "enabled" to createObject(false),
                    )
                ),
                //endregion
                //region Butter Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.butter",
                    imageName = "image.butter",
                    name = "upgrade.cheaper_butter.name",
                    id = "upgrade.cheaper_butter",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(2.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(2.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.butter"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.butter",
                    imageName = "image.butter",
                    iconName = "image.arrow.green_up",
                    name = "upgrade.dense_butter.name",
                    id = "upgrade.dense_butter",
                    price = 7,
                    cakeTier = 1,
                    maxLevel = 7,
                    onBuy = JsonMathHelpers.createDenseItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(1.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(3.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(7.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.butter"),
                    )
                ),
                //endregion
                //region Egg Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.egg",
                    imageName = "image.egg",
                    name = "upgrade.cheaper_egg.name",
                    id = "upgrade.cheaper_egg",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(10.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(2.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.egg"),
                        "priceDivisor" to createObject(2.toBigDecimal()),
                        "slopeDivisor" to createObject(1.75.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.egg",
                    imageName = "image.egg",
                    iconName = "image.arrow.green_up",
                    name = "upgrade.dense_egg.name",
                    id = "upgrade.dense_egg",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = 24,
                    onBuy = JsonMathHelpers.createDenseItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(12.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(16.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(5.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(3.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.egg"),
                    )
                ),
                //endregion
                //region Flour Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.flour",
                    imageName = "image.flour",
                    name = "upgrade.cheaper_flour.name",
                    id = "upgrade.cheaper_flour",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(2.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(2.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.flour"),
                        "priceDivisor" to createObject(2.toBigDecimal()),
                        "slopeDivisor" to createObject(1.5.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.flour",
                    imageName = "image.flour",
                    iconName = "image.arrow.green_up",
                    name = "upgrade.dense_flour.name",
                    id = "upgrade.dense_flour",
                    price = 7,
                    cakeTier = 2,
                    maxLevel = 3,
                    onBuy = JsonMathHelpers.createDenseItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(0.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(1.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(7.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.flour"),
                    )
                ),
                //endregion
                //region Sugar Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.sugar",
                    imageName = "image.sugar",
                    name = "upgrade.cheaper_sugar.name",
                    id = "upgrade.cheaper_sugar",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(2.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(2.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.sugar"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.sugar",
                    imageName = "image.sugar",
                    iconName = "image.arrow.green_up",
                    name = "upgrade.dense_sugar.name",
                    id = "upgrade.dense_sugar",
                    price = 7,
                    cakeTier = 1,
                    maxLevel = 7,
                    onBuy = JsonMathHelpers.createDenseItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(1.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(3.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(7.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.sugar"),
                    )
                ),
                //endregion
                //region Vanilla Extract Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.vanilla_extract",
                    imageName = "image.vanilla_extract",
                    name = "upgrade.cheaper_vanilla_extract.name",
                    id = "upgrade.cheaper_vanilla_extract",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(10.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(2.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.vanilla_extract"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.5.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.vanilla_extract",
                    imageName = "image.vanilla_extract",
                    iconName = "image.arrow.green_up",
                    name = "upgrade.dense_vanilla_extract.name",
                    id = "upgrade.dense_vanilla_extract",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = 10,
                    onBuy = JsonMathHelpers.createDenseItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(4.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(9.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(5.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.vanilla_extract"),
                    )
                ),
                //endregion
                //region Baking Powder Upgrades
                Upgrade(
                    pageName = "upgrade.page_name.baking_powder",
                    imageName = "image.baking_powder",
                    name = "upgrade.cheaper_baking_powder.name",
                    id = "upgrade.cheaper_baking_powder",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(2.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(2.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.baking_powder"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "upgrade.page_name.baking_powder",
                    imageName = "image.baking_powder",
                    iconName = "image.arrow.green_up",
                    name = "upgrade.dense_baking_powder.name",
                    id = "upgrade.dense_baking_powder",
                    price = 7,
                    cakeTier = 1,
                    maxLevel = 7,
                    onBuy = JsonMathHelpers.createDenseItemSmall() + JsonMathHelpers.createLinearGrowthSmall(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(1.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(3.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(7.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "itemName" to createObject("globals.items.item.baking_powder"),
                    )
                ),
                //endregion
            ),
            ovenProgress = 0.0,
            ovenRunning = false,
            autoOvenEnabled = false,
            autoOrderCompleteEnabled = false,
            tempCakeTier = 1,
            customerSatisfaction = 50,
            orderCakeSettings = mapOf(
                1 to OrderCakeSettings(
                    90.0, 45.0,
                    1.05, 0.99,
                    5,
                    35.0, 25.0,
                    5, 1,
                    -10, -30
                ),
                2 to OrderCakeSettings(
                    120.0, 60.0,
                    1.2, 0.9,
                    3,
                    45.0, 30.0,
                    15, 10,
                    -5, -10
                ),
                3 to OrderCakeSettings(
                    150.0, 75.0,
                    1.3, 0.8,
                    2,
                    55.0, 35.0,
                    45, 30,
                    -1, -5
                )
            ),
            orders = listOf(),
            orderCakeTimeCounters = mapOf(),
            isFirstBake = true
        )

        init {
            if (GENERATE_UPGRADE_LOCALISATIONS)
                default.upgrades.forEach { upgrade ->
                    println("\"" + upgrade.name + "\" to \"" + convertUnderscoreToName(upgrade.name.split(".")[1]) + "\",")
                    println("\"" + upgrade.pageName + "\" to \"" + convertUnderscoreToName(upgrade.pageName.split(".")[2]) + "\",")
                }
        }

        val state = UIState(
            items = default.items,
            currentCakeTier = default.currentCakeTier,
            upgrades = default.upgrades,
            customerSatisfaction = default.customerSatisfaction,
            canBake = false
        )
    }
}
