package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.serializers.BigDecimalSerializer
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.Object
import com.davidlukash.jsonmath.data.ObjectType
import com.davidlukash.jsonmath.engine.normal.LanguageException
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.serialization.Serializable


@Serializable
data class Item(
    val name: String,
    val type: ItemType,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal? = null,
    val fastPriceGrowth: Boolean? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val total: BigDecimal? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val increment: BigDecimal? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val increaseSlope: BigDecimal? = null,
    val cakePriceAccountability: Map<Int, @Serializable(with = BigDecimalSerializer::class) BigDecimal>? = null,
    val cakePrices: Map<Int, @Serializable(with = BigDecimalSerializer::class) BigDecimal>? = null,
    val cakeTier: Int? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val salePrice: BigDecimal? = null,
) {
    fun toObject(): Object = createObject(
        mapOf(
            createObject("name") to createObject(name),
            createObject("type") to createObject(type.toString()),
            createObject("amount") to createObject(amount),
            createObject("price") to createObject(price),
            createObject("fastPriceGrowth") to createObject(fastPriceGrowth),
            createObject("total") to createObject(total),
            createObject("increment") to createObject(increment),
            createObject("increaseSlope") to createObject(increaseSlope),
            createObject("cakePriceAccountability") to createObject(
                cakePriceAccountability?.map { (key, value) ->
                    createObject(key.toBigDecimal()) to createObject(value)
                }?.toMap()
            ),
            createObject("cakePrices") to createObject(
                cakePrices?.map { (key, value) ->
                    createObject(key.toBigDecimal()) to createObject(value)
                }?.toMap()
            ),
            createObject("cakeTier") to createObject(cakeTier?.toBigDecimal()),
            createObject("salePrice") to createObject(salePrice),
        )
    )

    fun mergeWith(other: Object) = fromObject(
        createObject(
            this.toObject().asDictionary()!! + other.asDictionary("Item")
        )
    )

    companion object {
        fun fromObject(obj: Object): Item {
            val dictionary = obj.asDictionary("Item")
            val cakePriceAccountabilityException = createInvalidTypeException(
                "cakePriceAccountability",
                "Dictionary[Integer, Number]"
            )
            val cakePricesException = createInvalidTypeException(
                "cakePrices",
                "Dictionary[Integer, Number]"
            )
            return Item(
                name = dictionary[createObject("name")]?.asString() ?: throw createInvalidTypeException(
                    "name",
                    ObjectType.STRING
                ),
                type = ItemType.valueOf(
                    dictionary[createObject("type")]?.asString() ?: throw createInvalidTypeException(
                        "type",
                        ObjectType.STRING
                    )
                ),
                amount = dictionary[createObject("amount")]?.asNumber() ?: throw createInvalidTypeException(
                    "amount",
                    ObjectType.NUMBER
                ),
                price = dictionary[createObject("price")]?.asNullableNumber("Item[price]"),
                fastPriceGrowth = dictionary[createObject("fastPriceGrowth")]?.asNullableBoolean("Item[fastPriceGrowth]"),
                total = dictionary[createObject("total")]?.asNullableNumber("Item[total]"),
                increment = dictionary[createObject("increment")]?.asNullableNumber("Item[increment]"),
                increaseSlope = dictionary[createObject("increaseSlope")]?.asNullableNumber("Item[increaseSlope]"),
                cakePriceAccountability = dictionary[createObject("cakePriceAccountability")]
                    ?.asDictionary()?.map { (key, value) ->
                        (key.asInteger()?.intValue(true) ?: throw cakePriceAccountabilityException) to
                                (value.asNumber() ?: throw cakePriceAccountabilityException)
                    }?.toMap(),
                cakePrices = dictionary[createObject("cakePrices")]
                    ?.asDictionary()?.map { (key, value) ->
                        (key.asInteger()?.intValue(true) ?: throw cakePricesException) to
                                (value.asNumber() ?: throw cakePricesException)
                    }?.toMap(),
                cakeTier = dictionary[createObject("cakeTier")]?.asNullableInteger("Item[cakeTier]")?.intValue(true),
                salePrice = dictionary[createObject("salePrice")]?.asNullableNumber("Item[salePrice]")
            )
        }

        fun createInvalidTypeException(
            field: String,
            requiredType: ObjectType?,
            requiredNullable: Boolean = false,
        ): LanguageException =
            LanguageException(
                message = "Item[$field] must be a ${requiredType?.toNaturalName() ?: "Any"}${if (requiredNullable) "?" else ""}",
                exceptionType = "InvalidTypeException",
            )

        fun createInvalidTypeException(
            field: String,
            requiredType: String,
        ): LanguageException =
            LanguageException(
                message = "Item[$field] must be a $requiredType",
                exceptionType = "InvalidTypeException",
            )
    }
}
