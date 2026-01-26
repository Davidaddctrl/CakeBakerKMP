package com.davidlukash.cakebaker.data.order

import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.mapDouble
import com.davidlukash.cakebaker.mapDoubleBiased
import com.davidlukash.cakebaker.takeOrNullWithWarn
import com.davidlukash.cakebaker.weightedRandom
import com.davidlukash.cakebaker.weightedRandomInt
import com.davidlukash.cakebaker.weightedRandomItem
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi

class OrderFactory(
    val addTextPopup: (String) -> Unit
) {
    fun selectCakeTier(
        cakes: Map<Int, Item>,
        customerSatisfaction: Int,
        random: Random,
        exclusions: List<Int> = listOf()
    ): Int? {
        val cakes = cakes.filter { it.value.amount != BigDecimal.ZERO }
        if (cakes.isEmpty())
            return null
        val weight = mapDoubleBiased(customerSatisfaction.toDouble(), 1.0, 100.0, 0.5, 2.5, -1.5)
        val cakeTiers = cakes.keys.filter { it !in exclusions }
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
    ): Order? {
        val weight = mapDoubleBiased(customerSatisfaction.toDouble(), 1.0, 100.0, 0.5, 2.5, -1.5)
        val settings =
            orderCakeSettings[cakeTier].takeOrNullWithWarn("Order cake settings with tier $cakeTier does not exist")
        if (settings == null) {
            addTextPopup("Failed to create order")
            return null
        }
        val cake = cakes[cakeTier].takeOrNullWithWarn("Cake with tier $cakeTier does not exist")
        if (cake == null) {
            addTextPopup("Failed to create order")
            return null
        }
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
            allocatedTime,
            random.nextInt(10000, 99999)
        )
    }
}