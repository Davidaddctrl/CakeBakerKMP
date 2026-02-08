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
                return save
            }
        }

        val migrations = listOf(
            migration0to1,
        )
    }
}