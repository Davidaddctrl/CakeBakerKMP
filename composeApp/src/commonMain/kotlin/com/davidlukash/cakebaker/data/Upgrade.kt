package com.davidlukash.cakebaker.data

import com.davidlukash.jsonmath.createNullObject
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.Expression
import com.davidlukash.jsonmath.data.Object
import com.davidlukash.jsonmath.data.ObjectType
import com.davidlukash.jsonmath.engine.normal.LanguageException
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlin.math.max

data class Upgrade(
    val pageName: String,
    val imageName: String,
    val name: String,
    val price: Int,
    val cakeTier: Int,
    val maxLevel: Int?,
    val onBuy: List<Expression>,
    val level: Int = 0,
    val parameters: Map<String, Object> = mapOf(),
) {
    fun toObject(): Object = createObject(
        mapOf(
            createObject("pageName") to createObject(pageName),
            createObject("imageName") to createObject(imageName),
            createObject("name") to createObject(name),
            createObject("price") to createObject(price.toBigDecimal()),
            createObject("cakeTier") to createObject(cakeTier.toBigDecimal()),
            createObject("maxLevel") to (
                    maxLevel?.let { createObject(it.toBigDecimal()) } ?: createNullObject()
                    ),
            createObject("onBuy") to createObject(onBuy.map { createObject(it) }),
            createObject("level") to createObject(level.toBigDecimal()),
            createObject("parameters") to createObject(
                parameters.map { createObject(it.key) to it.value }.toMap()
            ),
        )
    )

    fun mergeWith(other: Object) = fromObject(
        createObject(
            this.toObject().asDictionary()!! + other.asDictionary("Upgrade")
        )
    )

    companion object {
        fun fromObject(obj: Object): Upgrade {
            val dictionary = obj.asDictionary("Upgrade")
            if (!dictionary.contains(createObject("maxLevel"))) {
                throw createInvalidTypeException(
                    "maxLevel",
                    "Integer?",
                )
            }
            val maxLevelObject = dictionary[createObject("maxLevel")]!!
            val maxLevel =
                if (maxLevelObject.objectType == ObjectType.NULL) null else maxLevelObject.asInteger("Upgrade[maxLevel]")
                    .intValue(true)
            if (!dictionary.contains(createObject("onBuy"))) {
                throw createInvalidTypeException(
                    "onBuy",
                    "List[Expression]",
                )
            }
            val onBuy = dictionary[createObject("onBuy")]!!.asList("Upgrade[onBuy]").map {
                it.asExpression() ?: throw createInvalidTypeException(
                    "onBuy",
                    "List[Expression]",
                )
            }
            return Upgrade(
                pageName = dictionary[createObject("pageName")]?.asString() ?: throw createInvalidTypeException(
                    "pageName",
                    ObjectType.STRING
                ),
                imageName = dictionary[createObject("imageName")]?.asString() ?: throw createInvalidTypeException(
                    "imageName",
                    ObjectType.STRING
                ),
                name = dictionary[createObject("name")]?.asString() ?: throw createInvalidTypeException(
                    "name",
                    ObjectType.STRING
                ),
                price = (dictionary[createObject("price")]?.asNumber() ?: throw createInvalidTypeException(
                    "price",
                    ObjectType.NUMBER
                )).let {
                    if (!it.isWholeNumber()) throw createInvalidTypeException("price", "Integer")
                    it.intValue(true)
                },
                cakeTier = (dictionary[createObject("cakeTier")]?.asNumber() ?: throw createInvalidTypeException(
                    "cakeTier",
                    "Integer"
                )).let {
                    if (!it.isWholeNumber()) throw createInvalidTypeException("cakeTier", "Integer")
                    it.intValue(true)
                },
                maxLevel = maxLevel,
                onBuy = onBuy,
                level = (dictionary[createObject("level")]?.asNumber() ?: throw createInvalidTypeException(
                    "level",
                    ObjectType.NUMBER
                )).let {
                    if (!it.isWholeNumber()) throw createInvalidTypeException("level", "Integer")
                    it.intValue(true)
                },
            )
        }

        fun createInvalidTypeException(
            field: String,
            requiredType: ObjectType?,
            requiredNullable: Boolean = false,
        ): LanguageException =
            LanguageException(
                message = "Upgrade[$field] must be a ${requiredType?.toNaturalName() ?: "Any"}${if (requiredNullable) "?" else ""}",
                exceptionType = "InvalidTypeException",
            )

        fun createInvalidTypeException(
            field: String,
            requiredType: String,
        ): LanguageException =
            LanguageException(
                message = "Upgrade[$field] must be a $requiredType",
                exceptionType = "InvalidTypeException",
            )
    }
}