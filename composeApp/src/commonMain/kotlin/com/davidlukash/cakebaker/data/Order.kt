package com.davidlukash.cakebaker.data

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class Order(
    val cakeTier: Int,
    val amount: Int,
    val salePrice: BigDecimal,
    val remainingTime: Double,
    val totalTime: Double,
    val id: Int = 0
)
