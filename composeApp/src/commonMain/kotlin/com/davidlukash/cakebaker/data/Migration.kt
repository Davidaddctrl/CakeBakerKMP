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

            override fun migrate(save: Save): Save {
                return save.copy(
                    version = "Beta 0.9.2",
                    versionCode = 1,
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