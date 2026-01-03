package com.davidlukash.cakebaker.viewmodel

import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.Object
import com.davidlukash.jsonmath.data.ObjectType
import com.davidlukash.jsonmath.engine.normal.*

class CakeBakerScope(
    scopeType: ScopeType,
    val dataViewModel: DataViewModel
) : Scope(scopeType) {
    val localVariables = mutableMapOf<String, Object>()

    fun generateUpgradeDescriptors(): List<VariableDescriptor> {
        return dataViewModel.upgradesFlow.value.flatMap { upgrade ->
            val directDescriptors = upgrade.toObject().asDictionary()!!.flatMap { (key, value) ->
                val keyString = key.asString()!!
                listOf(
                    VariableDescriptor(
                        name = "globals.upgrades.${upgrade.name}.$keyString",
                        expectedType = when (keyString) {
                            "pageName" -> ObjectType.STRING
                            "imageName" -> ObjectType.STRING
                            "name" -> ObjectType.STRING
                            "price" -> ObjectType.NUMBER
                            "cakeTier" -> ObjectType.NUMBER
                            "maxLevel" -> ObjectType.NUMBER
                            "onBuy" -> ObjectType.LIST
                            "level" -> ObjectType.NUMBER
                            "parameters" -> ObjectType.DICTIONARY
                            else -> null
                        },
                        expectedTypeNullable = keyString == "maxLevel",
                        set = {
                            dataViewModel.updateUpgrade(
                                upgrade.mergeWith(
                                    createObject(
                                        mapOf(
                                            key to it
                                        )
                                    )
                                )
                            )
                        }
                    ) { value }
                ) + if (keyString == "parameters") {
                    value.asDictionary()!!.map { (paramKey, paramValue) ->
                        val paramKeyString = paramKey.asString()!!
                        VariableDescriptor(
                            name = "globals.upgrades.${upgrade.name}.parameters.$paramKeyString",
                            set = { paramNewValue ->
                                dataViewModel.updateUpgrade(
                                    upgrade.copy(
                                        parameters = upgrade.parameters + mapOf(
                                            paramKeyString to paramNewValue
                                        )
                                    )
                                )
                            },
                        ) { paramValue }
                    }
                } else listOf()
            }
            listOf(
                VariableDescriptor(
                    name = "globals.upgrades.${upgrade.name}",
                    expectedType = ObjectType.DICTIONARY,
                    expectedTypeNullable = false,
                    set = {
                        dataViewModel.updateUpgrade(
                            upgrade.mergeWith(it)
                        )
                    }
                ) {
                    upgrade.toObject()
                }
            ) + directDescriptors
        } + VariableDescriptor(
            "globals.upgrades",
            expectedType = ObjectType.LIST,
            expectedTypeNullable = false,
            set = {
                dataViewModel.setUpgrades(
                    it.asList()!!.map { innerUpgrade ->
                        Upgrade.fromObject(innerUpgrade)
                    }
                )
            }
        ) {
            createObject(dataViewModel.upgradesFlow.value.map { it.toObject() })
        }
    }

    fun generateItemDescriptors(): List<VariableDescriptor> {
        return dataViewModel.allItemsFlow.value.flatMap { item ->
            val directDescriptors = item.toObject().asDictionary()!!.map { (key, value) ->
                val keyString = key.asString()!!
                VariableDescriptor(
                    name = "globals.items.${item.name}.$keyString",
                    expectedType = when (keyString) {
                        "name" -> ObjectType.STRING
                        "type" -> ObjectType.STRING
                        "amount" -> ObjectType.NUMBER
                        "price" -> ObjectType.NUMBER
                        "fastPriceGrowth" -> ObjectType.BOOLEAN
                        "total" -> ObjectType.NUMBER
                        "increment" -> ObjectType.NUMBER
                        "increaseSlope" -> ObjectType.NUMBER
                        "cakePriceAccountability" -> ObjectType.DICTIONARY
                        "cakePrices" -> ObjectType.DICTIONARY
                        "cakeTier" -> ObjectType.NUMBER
                        "salePrice" -> ObjectType.NUMBER
                        else -> ObjectType.NUMBER
                    },
                    expectedTypeNullable = keyString in listOf(
                        "price",
                        "fastPriceGrowth",
                        "total",
                        "increment",
                        "increaseSlope",
                        "cakePriceAccountability",
                        "cakePrices",
                        "cakeTier",
                        "salePrice"
                    ),
                    set = {
                        dataViewModel.updateItem(
                            item.mergeWith(
                                createObject(
                                    mapOf(
                                        key to it
                                    )
                                )
                            )
                        )
                    }
                ) {
                    value
                }
            }
            listOf(
                VariableDescriptor(
                    name = "globals.items.${item.name}",
                    expectedType = ObjectType.DICTIONARY,
                    expectedTypeNullable = false,
                    set = {
                        dataViewModel.updateItem(
                            item.mergeWith(it)
                        )
                    }
                ) {
                    item.toObject()
                }
            ) + directDescriptors
        } + VariableDescriptor(
            "globals.items",
            expectedType = ObjectType.LIST,
            expectedTypeNullable = false,
            set = {
                dataViewModel.setItems(
                    it.asList()!!.map { innerItem ->
                        Item.fromObject(innerItem)
                    }
                )
            }
        ) {
            createObject(dataViewModel.allItemsFlow.value.map { it.toObject() })
        }
    }

    override fun listVariables(): List<VariableDescriptor> {
        return localVariables.keys.map { key ->
            VariableDescriptor(
                name = "locals.$key",
                set = { localVariables[key] = it },
            ) { localVariables[key]!! }
        } + if (scopeType == ScopeType(EnumScopeType.GLOBAL)) {
            generateUpgradeDescriptors() + generateItemDescriptors()
        } else listOf()
    }

    override fun addVariable(
        name: String,
        value: Object,
    ) {
        val parts = name.split(".")
        if (parts.isEmpty()) throw createVariableNameInvalidException(name)
        val first = parts[0]
        when (first) {
            "locals" -> {
                if (scopeType.type == EnumScopeType.GLOBAL) throw LanguageException(
                    "Cannot set a local variable in a global scope",
                    "WrongScopeException",
                )
                if (parts.size != 2) throw createVariableNameInvalidException(name)
                val second = parts[1]
                localVariables[second] = value
            }

            "globals" -> {
                if (scopeType.type == EnumScopeType.LOCAL) throw LanguageException(
                    "Cannot set a global variable in a local scope",
                    "WrongScopeException",
                )
                val second =
                    parts.getOrNull(1) ?: throw createVariableNameInvalidException(name) // eg. upgrades
                when (second) {
                    "upgrades" -> {
                        val upgrades = dataViewModel.upgradesFlow.value
                        val third = parts.getOrNull(2) ?: throw createVariableNameInvalidException(
                            name,
                        ) //eg. Faster Oven
                        val upgrade = upgrades.find { it.name == third } ?: throw createVariableNameInvalidException(
                            name,
                        )
                        val fourth = parts.getOrNull(3) ?: throw createVariableNameInvalidException(
                            name,
                        ) //eg. parameters
                        val fifth = parts.getOrNull(4) ?: throw createVariableNameInvalidException(
                            name,
                        ) //eg. buysUntilIncrement

                        dataViewModel.updateUpgrade(
                            upgrade.copy(
                                parameters = upgrade.parameters + mapOf(
                                    fifth to value
                                )
                            )
                        )
                    }
                }
            }

            else -> throw createVariableNameInvalidException(name)
        }
    }

    private fun createVariableNameInvalidException(name: String): LanguageException =
        LanguageException(
            "Invalid variable name $name",
            "VariableNameInvalidException",
        )

}