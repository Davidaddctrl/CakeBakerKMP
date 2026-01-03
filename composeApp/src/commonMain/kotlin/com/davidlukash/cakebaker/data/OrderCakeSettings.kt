package com.davidlukash.cakebaker.data

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class OrderCakeSettings(
    // Weighted by customer satisfaction
    val allocatedTimeMax: Double,
    val allocatedTimeMin: Double,
    // We use the base cake price and multiply it by a random number
    // between sale max change and min change weighted by customer satisfaction
    val saleMaxChange: Double,
    val saleMinChange: Double,
    val maxAmount: Int,
    val waitTimeMax: Double,
    val waitTimeMin: Double,
    val maxFulfilledCustomerSatisfaction: Int,
    val minFulfilledCustomerSatisfaction: Int,
    val maxUnfulfilledCustomerSatisfaction: Int,
    val minUnfulfilledCustomerSatisfaction: Int,
)
