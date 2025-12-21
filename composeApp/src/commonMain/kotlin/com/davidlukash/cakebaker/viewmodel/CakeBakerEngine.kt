package com.davidlukash.cakebaker.viewmodel

import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.toInt
import com.davidlukash.jsonmath.BasicOperatorsEngine
import com.davidlukash.jsonmath.Operation
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
                    ?: throw NoSuchElementException("$reference not found.")
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

            "orderCakeSettings" -> {
                val tier = parts[1].toInt()
                val settings =
                    dataViewModel.orderCakeSettings.value[tier]
                        ?: throw NoSuchElementException("$reference not found.")
                val third = parts[2]
                when (third) {
                    "allocatedTimeMax" -> settings.allocatedTimeMax.toBigDecimal()
                    "allocatedTimeMin" -> settings.allocatedTimeMin.toBigDecimal()
                    "saleMaxChange" -> settings.saleMaxChange.toBigDecimal()
                    "saleMinChange" -> settings.saleMinChange.toBigDecimal()
                    "maxAmount" -> settings.maxAmount.toBigDecimal()
                    "waitTimeMax" -> settings.waitTimeMax.toBigDecimal()
                    "waitTimeMin" -> settings.waitTimeMin.toBigDecimal()
                    "maxFulfilledCustomerSatisfaction" -> settings.maxFulfilledCustomerSatisfaction.toBigDecimal()
                    "minFulfilledCustomerSatisfaction" -> settings.minFulfilledCustomerSatisfaction.toBigDecimal()
                    "maxUnfulfilledCustomerSatisfaction" -> settings.maxUnfulfilledCustomerSatisfaction.toBigDecimal()
                    "minUnfulfilledCustomerSatisfaction" -> settings.minUnfulfilledCustomerSatisfaction.toBigDecimal()
                    else -> throw NoSuchElementException("$reference not found.")
                }
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

            "orderCakeSettings" -> {
                val tier = parts[1].toInt()
                val settings =
                    dataViewModel.orderCakeSettings.value[tier]
                        ?: throw NoSuchElementException("$reference not found.")
                val third = parts[2]
                dataViewModel.updateOrderSettings(
                    tier,
                    when (third) {
                        "allocatedTimeMax" -> settings.copy(
                            allocatedTimeMax = value.doubleValue(false)
                        )
                        "allocatedTimeMin" -> settings.copy(
                            allocatedTimeMin = value.doubleValue(false)
                        )
                        "saleMaxChange" -> settings.copy(
                            saleMaxChange = value.doubleValue(false)
                        )
                        "saleMinChange" -> settings.copy(
                            saleMinChange = value.doubleValue(false)
                        )
                        "maxAmount" -> settings.copy(
                            maxAmount = value.intValue(false)
                        )
                        "waitTimeMax" -> settings.copy(
                            waitTimeMax = value.doubleValue(false)
                        )
                        "waitTimeMin" -> settings.copy(
                            waitTimeMin = value.doubleValue(false)
                        )
                        "maxFulfilledCustomerSatisfaction" -> settings.copy(
                            maxFulfilledCustomerSatisfaction = value.intValue(false)
                        )
                        "minFulfilledCustomerSatisfaction" -> settings.copy(
                            minFulfilledCustomerSatisfaction = value.intValue(false)
                        )
                        "maxUnfulfilledCustomerSatisfaction" -> settings.copy(
                            maxUnfulfilledCustomerSatisfaction = value.intValue(false)
                        )
                        "minUnfulfilledCustomerSatisfaction" -> settings.copy(
                            minUnfulfilledCustomerSatisfaction = value.intValue(false)
                        )
                        else -> throw NoSuchElementException("$reference not found.")
                    }
                )
            }

            else -> throw NoSuchElementException("$reference not found.")
        }
    }
}