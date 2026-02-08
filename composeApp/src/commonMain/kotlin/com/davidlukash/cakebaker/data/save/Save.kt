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
) {
    @OptIn(ForMigrationSupport::class)
    fun forcedMigration(): Save {
        var save = this
        val autoOven = save.upgrades.find { it.name == "Auto Oven" }
        if (autoOven != null) {
            if (save.autoOvenEnabled != null) {
                save = save.copy(
                    upgrades = save.upgrades.map {
                        if (it.name == "Auto Oven") {
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
                        if (it.name == "Auto Oven") {
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

        val autoOrderComplete = save.upgrades.find { it.name == "Auto Order Complete" }
        if (autoOrderComplete != null) {
            if (save.autoOrderCompleteEnabled != null) {
                save = save.copy(
                    upgrades = save.upgrades.map {
                        if (it.name == "Auto Order Complete") {
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
                        if (it.name == "Auto Order Complete") {
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

        val default = Save(
            version = VERSION,
            versionCode = VERSIONCODE,
            items = listOf(
                Item(
                    name = "Butter",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.2f),
                    price = BigDecimal.fromFloat(250f),
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
                    name = "Egg",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(4f),
                    price = BigDecimal.fromFloat(30f),
                    fastPriceGrowth = false,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.09f),
                    cakePriceAccountability = mapOf(
                        1 to BigDecimal.ZERO,
                        2 to BigDecimal.ZERO,
                        3 to BigDecimal.ZERO,
                    ),
                    cakePrices = mapOf(
                        1 to BigDecimal.fromFloat(4f),
                        2 to BigDecimal.fromFloat(8f),
                        3 to BigDecimal.fromFloat(16f),
                    ),
                ),
                Item(
                    name = "Flour",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.1f),
                    price = BigDecimal.fromFloat(400f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.25f),
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
                    name = "Sugar",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.2f),
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
                    name = "Vanilla Extract",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.5f),
                    price = BigDecimal.fromFloat(150f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0.5f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.05f),
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
                    name = "Baking Powder",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.2f),
                    price = BigDecimal.fromFloat(175f),
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
                    name = "Cocoa Powder",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0f),
                    price = BigDecimal.fromFloat(8000f),
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
                        1 to BigDecimal.fromFloat(0.0f),
                        2 to BigDecimal.fromFloat(0.5f),
                        3 to BigDecimal.fromFloat(0.0f),
                    ),
                ),
                Item(
                    name = "Honey Pot",
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
                    name = "Vanilla Cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 1,
                    salePrice = BigDecimal.fromFloat(1350f)
                ),
                Item(
                    name = "Chocolate Cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 2,
                    salePrice = BigDecimal.fromFloat(4000f)
                ),
                Item(
                    name = "Honey Cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 3,
                    salePrice = BigDecimal.fromFloat(6000f)
                ),
                Item(
                    name = "Money",
                    type = ItemType.CURRENCY,
                    amount = BigDecimal.fromFloat(0f),
                )
            ),
            currentCakeTier = 1,
            upgrades = listOf(
                //region Cake Upgrades
                Upgrade(
                    pageName = "Cake",
                    imageName = "Vanilla Cake",
                    name = "Expensive Vanilla Cakes",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createLinearGrowth() + JsonMathHelpers.createOperation("math.product"),
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
                        "variable" to createObject("globals.items.Vanilla Cake.salePrice"),
                        "argument" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Cake",
                    imageName = "Chocolate Cake",
                    name = "Expensive Chocolate Cakes",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createLinearGrowth() + JsonMathHelpers.createOperation("math.product"),
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
                        "variable" to createObject("globals.items.Chocolate Cake.salePrice"),
                        "argument" to createObject(1.4.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Cake",
                    imageName = "Honey Cake",
                    name = "Expensive Honey Cakes",
                    price = 6,
                    cakeTier = 2,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createLinearGrowth() + JsonMathHelpers.createOperation("math.product"),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(5.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(4.toBigDecimal()),
                        "initialPrice" to createObject(6.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "variable" to createObject("globals.items.Honey Cake.salePrice"),
                        "argument" to createObject(1.7.toBigDecimal()),
                    )
                ),
                //endregion
                //region Oven Upgrades
                Upgrade(
                    pageName = "Oven",
                    imageName = "Oven",
                    name = "Faster Oven",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = 45,
                    onBuy = JsonMathHelpers.createLinearGrowth(),
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
                    pageName = "Oven",
                    imageName = "Oven",
                    name = "Auto Oven",
                    price = 3,
                    cakeTier = 1,
                    maxLevel = 1,
                    onBuy = JsonMathHelpers.createLinearGrowth(),
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
                    pageName = "Orders",
                    imageName = "Happy Face",
                    name = "Auto Order Complete",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = 1,
                    onBuy = JsonMathHelpers.createLinearGrowth(),
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
                    pageName = "Butter",
                    imageName = "Butter",
                    name = "Cheaper Butter",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItem() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Butter"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Butter",
                    imageName = "Butter",
                    name = "Dense Butter",
                    price = 7,
                    cakeTier = 1,
                    maxLevel = 7,
                    onBuy = JsonMathHelpers.createDense() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Butter"),
                    )
                ),
                //endregion
                //region Egg Upgrades
                Upgrade(
                    pageName = "Egg",
                    imageName = "Egg",
                    name = "Cheaper Egg",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItem() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Egg"),
                        "priceDivisor" to createObject(2.toBigDecimal()),
                        "slopeDivisor" to createObject(1.75.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Egg",
                    imageName = "Egg",
                    name = "Dense Egg",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = 24,
                    onBuy = JsonMathHelpers.createDense() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Egg"),
                    )
                ),
                //endregion
                //region Flour Upgrades
                Upgrade(
                    pageName = "Flour",
                    imageName = "Flour",
                    name = "Cheaper Flour",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItem() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Flour"),
                        "priceDivisor" to createObject(2.toBigDecimal()),
                        "slopeDivisor" to createObject(1.5.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Flour",
                    imageName = "Flour",
                    name = "Dense Flour",
                    price = 7,
                    cakeTier = 2,
                    maxLevel = 3,
                    onBuy = JsonMathHelpers.createDense() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Flour"),
                    )
                ),
                //endregion
                //region Sugar Upgrades
                Upgrade(
                    pageName = "Sugar",
                    imageName = "Sugar",
                    name = "Cheaper Sugar",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItem() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Sugar"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Sugar",
                    imageName = "Sugar",
                    name = "Dense Sugar",
                    price = 7,
                    cakeTier = 1,
                    maxLevel = 7,
                    onBuy = JsonMathHelpers.createDense() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Sugar"),
                    )
                ),
                //endregion
                //region Vanilla Extract Upgrades
                Upgrade(
                    pageName = "Vanilla Extract",
                    imageName = "Vanilla Extract",
                    name = "Cheaper Vanilla Extract",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItem() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Vanilla Extract"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.5.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Vanilla Extract",
                    imageName = "Vanilla Extract",
                    name = "Dense Vanilla Extract",
                    price = 5,
                    cakeTier = 1,
                    maxLevel = 10,
                    onBuy = JsonMathHelpers.createDense() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Vanilla Extract"),
                    )
                ),
                //endregion
                //region Baking Powder Upgrades
                Upgrade(
                    pageName = "Baking Powder",
                    imageName = "Baking Powder",
                    name = "Cheaper Baking Powder",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createCheaperItem() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Baking Powder"),
                        "priceDivisor" to createObject(1.75.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Baking Powder",
                    imageName = "Baking Powder",
                    name = "Dense Baking Powder",
                    price = 7,
                    cakeTier = 1,
                    maxLevel = 7,
                    onBuy = JsonMathHelpers.createDense() + JsonMathHelpers.createLinearGrowth(),
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
                        "itemName" to createObject("globals.items.Baking Powder"),
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
            listOf(),
            mapOf()
        )

        val state = UIState(
            items = default.items,
            currentCakeTier = default.currentCakeTier,
            upgrades = default.upgrades,
            customerSatisfaction = default.customerSatisfaction,
            canBake = false
        )
    }
}
