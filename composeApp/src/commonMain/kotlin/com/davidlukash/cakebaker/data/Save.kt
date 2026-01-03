package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.JsonMathHelpers
import com.davidlukash.jsonmath.buildExpressionList
import com.davidlukash.jsonmath.createObject
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class Save(
    val items: List<Item>,
    val currentCakeTier: Int,
    val upgrades: List<Upgrade>,
    val ovenProgress: Double,
    val ovenRunning: Boolean,
    val autoOvenEnabled: Boolean,
    val tempCakeTier: Int,
    val customerSatisfaction: Int,
    val orderCakeSettings: Map<Int, OrderCakeSettings>,
    val orders: List<Order>,
    val orderCakeTimeCounters: Map<Int, Double>
) {
    companion object {
        val default = Save(
            items = listOf(
                Item(
                    name = "Butter",
                    type = ItemType.INGREDIENT,
                    amount = BigDecimal.fromFloat(0.2f),
                    price = BigDecimal.fromFloat(250f),
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
                    increaseSlope = BigDecimal.fromFloat(0.1f),
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
                    increaseSlope = BigDecimal.fromFloat(0.3f),
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
                    increaseSlope = BigDecimal.fromFloat(0.15f),
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
                    increaseSlope = BigDecimal.fromFloat(0.4f),
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
                    increaseSlope = BigDecimal.fromFloat(0.2f),
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
                    price = BigDecimal.fromFloat(4000f),
                    fastPriceGrowth = true,
                    total = BigDecimal.fromFloat(0f),
                    increment = BigDecimal.fromInt(1),
                    increaseSlope = BigDecimal.fromFloat(0.5f),
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
                    price = BigDecimal.fromFloat(7500f),
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
                    salePrice = BigDecimal.fromFloat(1400f)
                ),
                Item(
                    name = "Chocolate Cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 2,
                    salePrice = BigDecimal.fromFloat(8000f)
                ),
                Item(
                    name = "Honey Cake",
                    type = ItemType.CAKE,
                    amount = BigDecimal.ZERO,
                    cakeTier = 3,
                    salePrice = BigDecimal.fromFloat(17000f)
                ),
                Item(
                    name = "Money",
                    type = ItemType.CURRENCY,
                    amount = BigDecimal.fromFloat(0f),
                )
            ),
            currentCakeTier = 1,
            upgrades = listOf(
                Upgrade(
                    pageName = "Vanilla Cake",
                    imageName = "Vanilla Cake",
                    name = "Expensive Vanilla Cakes",
                    price = 2,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = buildExpressionList {
                        appendFunction {
                            name = "variable.set"
                            appendString("globals.items.Vanilla Cake.salePrice")
                            appendFunction {
                                name = "math.product"
                                appendFunction {
                                    name = "variable.get"
                                    appendString("globals.items.Vanilla Cake.salePrice")
                                    appendBoolean(true)
                                }
                                appendNumber("1.25")
                            }
                            appendBoolean(true)
                        }
                    } + JsonMathHelpers.createLinearOnBuy(),
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
                    )
                ),
                Upgrade(
                    pageName = "Oven",
                    imageName = "Oven",
                    name = "Faster Oven",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = 45,
                    onBuy = JsonMathHelpers.createLinearOnBuy(),
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
                    onBuy = JsonMathHelpers.createLinearOnBuy(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(mapOf()),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Egg",
                    imageName = "Egg",
                    name = "Cheaper Egg",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createItemCheaperUpgradeOnBuy() + JsonMathHelpers.createLinearOnBuy(),
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
                        "priceDivisor" to createObject(2.5.toBigDecimal()),
                        "slopeDivisor" to createObject(1.5.toBigDecimal()),
                    )
                ),
                Upgrade(
                    pageName = "Vanilla Extract",
                    imageName = "Vanilla Extract",
                    name = "Cheaper Vanilla Extract",
                    price = 1,
                    cakeTier = 1,
                    maxLevel = null,
                    onBuy = JsonMathHelpers.createItemCheaperUpgradeOnBuy() + JsonMathHelpers.createLinearOnBuy(),
                    parameters = mapOf(
                        "cakeTiers" to createObject(
                            mapOf(
                                createObject(10.toBigDecimal()) to createObject(2.toBigDecimal()),
                                createObject(20.toBigDecimal()) to createObject(3.toBigDecimal()),
                            )
                        ),
                        "priceIncrement" to createObject(1.toBigDecimal()),
                        "initialPrice" to createObject(1.toBigDecimal()),
                        "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                        "itemName" to createObject("globals.items.Vanilla Extract"),
                        "priceDivisor" to createObject(3.toBigDecimal()),
                        "slopeDivisor" to createObject(1.25.toBigDecimal()),
                    )
                ),
            ),
            ovenProgress = 0.0,
            ovenRunning = false,
            autoOvenEnabled = false,
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
    }
}
