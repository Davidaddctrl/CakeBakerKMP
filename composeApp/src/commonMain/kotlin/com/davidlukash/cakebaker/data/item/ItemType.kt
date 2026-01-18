package com.davidlukash.cakebaker.data.item

import kotlinx.serialization.Serializable

@Serializable
enum class ItemType {
    INGREDIENT,
    CAKE,
    CURRENCY
}