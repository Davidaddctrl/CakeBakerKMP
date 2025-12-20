package com.davidlukash.cakebaker.data

import com.ionspin.kotlin.bignum.decimal.BigDecimal


data class Item(
    val name: String,
    val type: ItemType,
    val amount: BigDecimal,
    val price: BigDecimal? = null,
    val fastPriceGrowth: Boolean? = null,
    val total: BigDecimal? = null,
    val increment: BigDecimal? = null,
    val increaseSlope: BigDecimal? = null,
    val cakePriceAccountability: Map<Int, BigDecimal>?= null,
    val cakePrices: Map<Int, BigDecimal>? = null,
    val cakeTier: Int? = null,
    val salePrice: BigDecimal? = null,
)
