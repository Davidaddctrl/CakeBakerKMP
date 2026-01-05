package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.toBoolean
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlin.math.ceil

data class UIState(
    val items: List<Item>,
    val currentCakeTier: Int,
    val upgrades: List<Upgrade>,
    val ovenProgress: Double,
    val ovenRunning: Boolean,
    val autoOvenEnabled: Boolean,
    val customerSatisfaction: Int,
    val orders: List<Order>,
    val nextOrderRemainingTime: Double?,
    val canBake: Boolean,
) {
    companion object {
        val default = UIState(
            items = listOf(),
            currentCakeTier = 1,
            upgrades = listOf(),
            ovenProgress = 0.0,
            ovenRunning = false,
            autoOvenEnabled = false,
            customerSatisfaction = 1,
            orders = listOf(),
            nextOrderRemainingTime = 0.0,
            canBake = false,
        )
    }

    fun getMoneyItem(): Item = items.find { it.name == "Money" } ?: Item(
        "Money",
        ItemType.CURRENCY,
        BigDecimal.ZERO
    )

    fun getIngredients(): List<Item> = items.filter { it.type == ItemType.INGREDIENT }

    fun getAutoOven(): Boolean? = upgrades.find { it.name == "Auto Oven" }?.level?.toBoolean()

    fun getFasterOven(): Double = upgrades.find { it.name == "Faster Oven" }?.level?.toDouble() ?: 0.0

    fun calculateCakePrice(cake: Item, ingredients: List<Item>): BigDecimal {
        var cakePrice = cake.salePrice ?: BigDecimal.ZERO
        ingredients.forEach { ingredient ->
            cakePrice += ingredient.cakePriceAccountability?.get(cake.cakeTier ?: 1) ?: BigDecimal.ZERO
        }
        return cakePrice
    }

    fun getCakes(): Map<Int, Item> =
        items.filter { it.type == ItemType.CAKE && it.cakeTier != null }.associateBy { it.cakeTier!! }

    fun getCakesSalesPrices(): Map<Int, BigDecimal> = getCakes().map {
        it.key to calculateCakePrice(it.value, getIngredients())
    }.toMap()

    fun getSatisfactionLevel(): Int = ceil(customerSatisfaction.toDouble() / 20.0).toInt()
}
