package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.save.Save

interface Migration {
    val from: Int
    val to: Int

    fun migrate(save: Save): Save

    companion object {
        val migration0to1 = object : Migration {
            override val from: Int = 0
            override val to: Int = 1
            private val itemNameToId = mapOf(
                "Butter" to "item.butter",
                "Egg" to "item.egg",
                "Flour" to "item.flour",
                "Sugar" to "item.sugar",
                "Vanilla Extract" to "item.vanilla_extract",
                "Baking Powder" to "item.baking_powder",
                "Cocoa Powder" to "item.cocoa_powder",
                "Honey Pot" to "item.honey_pot",
                "Vanilla Cake" to "item.vanilla_cake",
                "Chocolate Cake" to "item.chocolate_cake",
                "Honey Cake" to "item.honey_cake",
                "Money" to "item.money"
            )

            val upgradeNameToId = mapOf(
                "Expensive Vanilla Cakes" to "upgrade.expensive_vanilla_cakes",
                "Expensive Chocolate Cakes" to "upgrade.expensive_chocolate_cakes",
                "Expensive Honey Cakes" to "upgrade.expensive_honey_cakes",
                "Faster Oven" to "upgrade.faster_oven",
                "Auto Oven" to "upgrade.auto_oven",
                "Cheaper Egg" to "upgrade.cheaper_egg",
                "Cheaper Vanilla Extract" to "upgrade.cheaper_vanilla_extract"
            )

            override fun migrate(save: Save): Save {
                return save
                    .copy(
                    version = "Beta 0.9.2",
                    versionCode = 1,
                    items = Save.default.items.map { baseItem ->
                        val item = save.items.find { itemNameToId[it.name] == baseItem.id }
                        item?.let {
                            baseItem.copy(
                                amount = item.amount,
                                price = item.price,
                                total = item.total,
                                increaseSlope = item.increaseSlope
                            )
                        } ?: baseItem
                    },
                    upgrades = Save.default.upgrades.map { baseUpgrade ->
                        val upgrade = save.upgrades.find { upgradeNameToId[it.name] == baseUpgrade.id }
                        upgrade?.let {
                             baseUpgrade.copy(
                                 price = upgrade.price,
                                 cakeTier = upgrade.cakeTier,
                                 level = upgrade.level,
                             )
                        } ?: baseUpgrade
                    }
                )
            }
        }

        val migrations = listOf(
            migration0to1,
        )
    }
}