package com.davidlukash.cakebaker.data

import kotlinx.serialization.Serializable

@Serializable
enum class ItemType {
    INGREDIENT,
    CAKE,
    CURRENCY
}