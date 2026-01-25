package com.davidlukash.cakebaker.data.order

import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.mapDouble
import com.davidlukash.cakebaker.mapDoubleBiased
import com.davidlukash.cakebaker.weightedRandom
import com.davidlukash.cakebaker.weightedRandomInt
import com.davidlukash.cakebaker.weightedRandomItem
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi

class OrderFactory {
    fun selectCakeTier(
        cakes: Map<Int, Item>,
        customerSatisfaction: Int,
        random: Random,
        exclusions: List<Int> = listOf()
    ): Int? {
        val cakes = cakes.filter { it.value.amount != BigDecimal.ZERO }
        if (cakes.isEmpty()) return null
        val weight = mapDoubleBiased(customerSatisfaction.toDouble(), 1.0, 100.0, 0.5, 2.5, -1.5)
        val random = random
        val cakeTiers = cakes.map { it.value.cakeTier ?: 1 }.filter { it !in exclusions }
        val cakeTier = weightedRandomItem(weight, cakeTiers, random)
        return cakeTier
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createOrder(
        cakeTier: Int,
        cakes: Map<Int, Item>,
        customerSatisfaction: Int,
        random: Random,
        orderCakeSettings: Map<Int, OrderCakeSettings>,
        baseCakePrice: BigDecimal,
    ): Order {
        val weight = mapDoubleBiased(customerSatisfaction.toDouble(), 1.0, 100.0, 0.5, 2.5, -1.5)
        val random = random
        val settings = orderCakeSettings[cakeTier]
            ?: throw IllegalArgumentException("Order Cake Settings with tier $cakeTier does not exist")
        val cake = cakes[cakeTier]
            ?: throw IllegalArgumentException("Cake with tier $cakeTier does not exist")
        val maxAmount = settings.maxAmount
        val cakeAmount = minOf(
            maxOf(cake.amount.doubleValue(false).toInt(), 1),
            weightedRandomInt(weight, maxAmount, random) + 1
        )
        val cakePriceModifier =
            mapDouble(
                weightedRandom(weight, 1.0, random),
                0.0, 1.0,
                settings.saleMinChange, settings.saleMaxChange
            ).toBigDecimal()
        val cakePrice = baseCakePrice.multiply(cakePriceModifier, globalDecimalMode)
        val allocatedTime =
            mapDouble(
                weightedRandom(weight, 1.0, random),
                0.0, 1.0,
                settings.allocatedTimeMin, settings.allocatedTimeMax
            )
        return Order(
            cakeTier,
            cakeAmount,
            cakePrice * cakeAmount,
            allocatedTime,
            allocatedTime
        )
    }
}