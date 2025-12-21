package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.mapDouble
import com.davidlukash.cakebaker.toDouble
import com.davidlukash.cakebaker.viewmodel.DataViewModel
import com.davidlukash.cakebaker.weightedRandom
import com.davidlukash.cakebaker.weightedRandomInt
import com.davidlukash.cakebaker.weightedRandomItem
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlin.math.ceil
import kotlin.uuid.ExperimentalUuidApi

class OrderFactory(
    val dataViewModel: DataViewModel,
) {
    fun selectCakeTier(exclusions: List<Int> = listOf()): Int? {
        val cakes = dataViewModel.cakes.filter { it.amount != BigDecimal.ZERO }
        if (cakes.isEmpty()) return null
        val weight = mapDouble(dataViewModel.customerSatisfaction.value.toDouble(), 1.0, 100.0, 0.5, 2.5)
        val random = dataViewModel.random
        val cakeTiers = cakes.map { it.cakeTier ?: 1 }.filter { it !in exclusions }
        val cakeTier = weightedRandomItem(weight, cakeTiers, random)
        return cakeTier
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createOrder(cakeTier: Int): Order {
//        val cakes = dataViewModel.cakes.filter { it.amount != BigDecimal.ZERO }
//        if (cakes.isEmpty()) return null
//        val weight = mapDouble(dataViewModel.customerSatisfaction.value.toDouble(), 1.0, 100.0, 0.5, 2.0)
//        val random = dataViewModel.random
//        val cakeTiers = cakes.map { it.cakeTier ?: 1 }
//        val cakeTier = weightedRandomItem(weight, cakeTiers, random)
        val cakes = dataViewModel.cakes
        val weight = mapDouble(dataViewModel.customerSatisfaction.value.toDouble(), 1.0, 100.0, 0.5, 2.0)
        val random = dataViewModel.random
        val settings = dataViewModel.orderCakeSettings.value[cakeTier]!!
        val cake = cakes.find { it.cakeTier == cakeTier }!!
        val maxAmount = settings.maxAmount
        val cakeAmount = minOf(maxOf(cake.amount.doubleValue(false).toInt(), 1), weightedRandomInt(weight, maxAmount, random) + 1)
        val baseCakePrice = dataViewModel.calculateCakePrice(cakeTier)
        val cakePriceModifier =
            mapDouble(
                weightedRandom(weight, 1.0, random),
                0.0, 1.0,
                settings.saleMinChange, settings.saleMaxChange
            ).toBigDecimal()
        val cakePrice = baseCakePrice.multiply(cakePriceModifier).roundToDigitPositionAfterDecimalPoint(
            2L,
            RoundingMode.FLOOR
        )
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