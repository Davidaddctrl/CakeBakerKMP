package com.davidlukash.cakebaker

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

class PrecisionNumber(
    val value: Int
    ,
    val precision: Int = 2,
) {

    override fun equals(other: Any?): Boolean {
        return other.hashCode() == this.hashCode()
    }

    infix fun equals(number: Int): Boolean {
        return (number * 10.0.pow(precision)).roundToInt() == value
    }

    fun toDouble(): Double {
        return (value / 10.0.pow(precision))
    }

    override fun toString(): String {
        return (value / 10.0.pow(precision)).toString()
    }

    operator fun compareTo(number: PrecisionNumber): Int {
        if (number.precision != precision) {
            throw Exception("Precisions must be equal")
        }
        return value.compareTo(number.value)
    }

    operator fun compareTo(number: Int): Int {
        return compareTo(PrecisionNumber((number * 10.0.pow(precision)).roundToInt(), precision))
    }

    operator fun plus(number: PrecisionNumber): PrecisionNumber {
        if (number.precision != precision) {
            throw Exception("Precisions must be equal")
        }
        return PrecisionNumber(
            value + number.value,
            precision
        )
    }

    operator fun plus(number: Int): PrecisionNumber {
        return plus(PrecisionNumber((number * 10.0.pow(precision)).roundToInt(), precision))
    }

    operator fun minus(number: PrecisionNumber): PrecisionNumber {
        if (number.precision != precision) {
            throw Exception("Precisions must be equal")
        }
        return PrecisionNumber(
            value - number.value,
            precision
        )
    }

    operator fun minus(number: Int): PrecisionNumber {
        return minus(PrecisionNumber((number * 10.0.pow(precision)).roundToInt(), precision))
    }

    operator fun times(number: PrecisionNumber): PrecisionNumber {
        if (number.precision != precision) {
            throw Exception("Precisions must be equal")
        }
        return PrecisionNumber(
            (
                    (value * number.value).toDouble() /
                            10.0.pow((precision).toDouble())
                    ).roundToInt(),
            precision
        )
    }

    operator fun times(number: Int): PrecisionNumber {
        return times(PrecisionNumber((number * 10.0.pow(precision)).roundToInt(), precision))
    }

    operator fun div(number: PrecisionNumber): PrecisionNumber {
        if (number.precision != precision) {
            throw Exception("Precisions must be equal")
        }
        return PrecisionNumber(
            (
                    (value.toDouble() / number.value.toDouble()) *
                            10.0.pow((precision).toDouble())
                    ).roundToInt(),
            precision
        )
    }

    operator fun div(number: Int): PrecisionNumber {
        return div(PrecisionNumber((number.toDouble() * 10.0.pow(precision)).roundToInt(), precision))
    }

    fun log10(): Int {
        return floor(log10(value.toDouble()) - precision.toInt()).toInt()
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + precision
        return result
    }
}