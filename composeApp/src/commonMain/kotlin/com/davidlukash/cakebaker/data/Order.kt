package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.serializers.BigDecimalSerializer
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val cakeTier: Int,
    val amount: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val salePrice: BigDecimal,
    val remainingTime: Double,
    val totalTime: Double,
    val id: Int = 0
)
