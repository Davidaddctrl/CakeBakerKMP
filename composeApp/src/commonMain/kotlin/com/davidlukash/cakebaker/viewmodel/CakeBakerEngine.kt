package com.davidlukash.cakebaker.viewmodel

import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.toInt
import com.davidlukash.jsonmath.BasicOperatorsEngine
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

class CakeBakerEngine(
    val mainViewModel: MainViewModel
) : BasicOperatorsEngine(
    decimalMode = DecimalMode(
        decimalPrecision = 0,
        roundingMode = RoundingMode.FLOOR,
        scale = 2
    )
) {
    override fun getReference(reference: String): BigDecimal {
        val parts = reference.split(".")
        val first = parts[0]
        val dataViewModel = mainViewModel.dataViewModel
        return when (first) {
            "items" -> {
                val itemName = parts[1]
                val item = dataViewModel.allItemsFlow.value.find { it.name == itemName }
                if (item == null) {
                    throw NoSuchElementException("$reference not found.")
                }
                val third = parts[2]
                when (third) {
                    "type" -> ItemType.entries.indexOf(item.type).toBigDecimal(decimalMode = decimalMode)
                    "amount" -> item.amount
                    "price" -> item.price
                    "fastPriceGrowth" -> item.fastPriceGrowth?.toInt()?.toBigDecimal(decimalMode = decimalMode)
                    "total" -> item.total
                    "increment" -> item.increment
                    "increaseSlope" -> item.increaseSlope
                    "cakePriceAccountability" -> {
                        val index = parts[3]
                        item.cakePriceAccountability?.get(index.toInt())
                    }

                    "cakePrices" -> {
                        val index = parts[3]
                        item.cakePrices?.get(index.toInt())
                    }

                    "cakeTier" -> item.cakeTier?.toBigDecimal(decimalMode = decimalMode)
                    "salePrice" -> item.salePrice
                    else -> throw NoSuchElementException("$reference not found.")
                } ?: BigDecimal.parseString("-1")
            }

            else -> throw NoSuchElementException("$reference not found.")
        }
    }

    override fun setReference(reference: String, value: BigDecimal) {
        val parts = reference.split(".")
        val first = parts[0]
        val dataViewModel = mainViewModel.dataViewModel
        when (first) {
            "items" -> {
                val itemName = parts[1]
                val item = dataViewModel.allItemsFlow.value.find { it.name == itemName }
                if (item == null) {
                    throw NoSuchElementException("$reference not found.")
                }
                val third = parts[2]
                dataViewModel.updateItem(
                    when (third) {
                        "type" -> item.copy(
                            type = ItemType.entries[value.intValue(false)]
                        )

                        "amount" -> item.copy(amount = value)
                        "price" -> item.copy(price = value)
                        "fastPriceGrowth" -> item.copy(fastPriceGrowth = value > BigDecimal.ZERO)
                        "total" -> item.copy(total = value)
                        "increment" -> item.copy(increment = value)
                        "increaseSlope" -> item.copy(increaseSlope = value)
                        "cakePriceAccountability" -> {
                            val index = parts[3].toInt()
                            item.copy(
                                cakePriceAccountability = (item.cakePriceAccountability ?: mapOf()) + (index to value)
                            )
                        }

                        "cakePrices" -> {
                            val index = parts[3].toInt()
                            item.copy(
                                cakePrices = (item.cakePrices ?: mapOf()) + (index to value)
                            )
                        }

                        "cakeTier" -> item.copy(cakeTier = value.intValue(false))
                        "salePrice" -> item.copy(salePrice = value)
                        else -> throw NoSuchElementException("$reference not found.")
                    }
                )
            }

            else -> throw NoSuchElementException("$reference not found.")
        }
    }
}