package com.davidlukash.cakebaker.data.order

import com.davidlukash.cakebaker.data.serializers.BigDecimalSerializer
import com.davidlukash.cakebaker.roundTo1dp
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.Object
import com.davidlukash.jsonmath.data.ObjectType
import com.davidlukash.jsonmath.engine.normal.LanguageException
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val cakeTier: Int,
    val amount: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val salePrice: BigDecimal,
    val remainingTime: Double,
    val totalTime: Double,
    val id: Int = 0
) {
    fun toObject(): Object = createObject(
        mapOf(
            createObject("cakeTier") to createObject(cakeTier.toBigDecimal()),
            createObject("amount") to createObject(amount.toBigDecimal()),
            createObject("salePrice") to createObject(salePrice),
            createObject("remainingTime") to createObject(remainingTime.toBigDecimal()),
            createObject("totalTime") to createObject(totalTime.toBigDecimal()),
            createObject("id") to createObject(id.toBigDecimal()),
        )
    )

    fun mergeWith(other: Object) = fromObject(
        createObject(
            this.toObject().asDictionary()!! + other.asDictionary("Order")
        )
    )

    companion object {
        fun fromObject(obj: Object): Order {
            val dictionary = obj.asDictionary("Order")
            
            return Order(
                cakeTier = (dictionary[createObject("cakeTier")]?.asInteger() ?: throw createInvalidTypeException(
                    "cakeTier",
                    "Integer"
                )).intValue(true),
                amount = (dictionary[createObject("amount")]?.asInteger() ?: throw createInvalidTypeException(
                    "amount",
                    "Integer"
                )).intValue(true),
                salePrice = dictionary[createObject("salePrice")]?.asNumber()?.roundTo1dp() ?: throw createInvalidTypeException(
                    "salePrice",
                    ObjectType.NUMBER
                ),
                remainingTime = (dictionary[createObject("remainingTime")]?.asNumber() ?: throw createInvalidTypeException(
                    "remainingTime",
                    ObjectType.NUMBER
                )).doubleValue(false),
                totalTime = (dictionary[createObject("totalTime")]?.asNumber() ?: throw createInvalidTypeException(
                    "totalTime",
                    ObjectType.NUMBER
                )).doubleValue(false),
                id = (dictionary[createObject("id")]?.asInteger() ?: throw createInvalidTypeException(
                    "id",
                    "Integer"
                )).intValue(true),
            )
        }

        fun createInvalidTypeException(
            field: String,
            requiredType: ObjectType?,
            requiredNullable: Boolean = false,
        ): LanguageException =
            LanguageException(
                message = "Order[$field] must be a ${requiredType?.toNaturalName() ?: "Any"}${if (requiredNullable) "?" else ""}",
                exceptionType = "InvalidTypeException",
            )

        fun createInvalidTypeException(
            field: String,
            requiredType: String,
        ): LanguageException =
            LanguageException(
                message = "Order[$field] must be a $requiredType",
                exceptionType = "InvalidTypeException",
            )
    }
}
