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

            private val nameToName = mapOf(
                "Butter" to "item.butter.name",
                "Egg" to "item.egg.name",
                "Flour" to "item.flour.name",
                "Sugar" to "item.sugar.name",
                "Vanilla Extract" to "item.vanilla_extract.name",
                "Baking Powder" to "item.baking_powder.name",
                "Cocoa Powder" to "item.cocoa_powder.name",
                "Honey Pot" to "item.honey_pot.name",
                "Vanilla Cake" to "item.vanilla_cake.name",
                "Chocolate Cake" to "item.chocolate_cake.name",
                "Honey Cake" to "item.honey_cake.name",
            )

            private val nameToId = mapOf(
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
            )
            
            private val nameToImage = mapOf(
                "Butter" to "image.butter",
                "Egg" to "image.egg",
                "Flour" to "image.flour",
                "Sugar" to "image.sugar",
                "Vanilla Extract" to "image.vanilla_extract",
                "Baking Powder" to "image.baking_powder",
                "Cocoa Powder" to "image.cocoa_powder",
                "Honey Pot" to "image.honey_pot",
                "Vanilla Cake" to "image.vanilla_cake",
                "Chocolate Cake" to "image.chocolate_cake",
                "Honey Cake" to "image.honey_cake",
            )

            override fun migrate(save: Save): Save {
                return save.copy(
                    version = "Beta 0.9.2",
                    versionCode = 1,
                    items = save.items.map { item ->
                        item.copy(
                            name = nameToName[item.name] ?: item.name,
                            id = nameToId[item.id] ?: item.id,
                            image = nameToImage[item.name] ?: item.image,
                        )
                    },
                    upgrades = Save.default.upgrades.map { baseUpgrade ->
                        val currentUpgrade = save.upgrades.find { it.name == baseUpgrade.name }
                        currentUpgrade?.copy(
                            pageName = baseUpgrade.pageName,
                            imageName = baseUpgrade.imageName,
                            onBuy = baseUpgrade.onBuy,
                            parameters = baseUpgrade.parameters,
                        )
                            ?: baseUpgrade
                    }
                )
            }
        }

        val migrations = listOf(
            migration0to1,
        )
    }
}