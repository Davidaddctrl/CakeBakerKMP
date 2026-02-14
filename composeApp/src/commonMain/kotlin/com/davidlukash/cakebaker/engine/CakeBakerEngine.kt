package com.davidlukash.cakebaker.engine

import com.davidlukash.cakebaker.JsonMathHelpers.registerHelpers
import com.davidlukash.cakebaker.JsonMathHelpers.registerUIActions
import com.davidlukash.cakebaker.data.DataActions
import com.davidlukash.cakebaker.dumpFunctionsToFile
import com.davidlukash.cakebaker.logger
import com.davidlukash.cakebaker.viewmodel.UIViewModel
import com.davidlukash.jsonmath.engine.normal.Engine
import com.davidlukash.jsonmath.engine.normal.ScopeType
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode

class CakeBakerEngine : Engine<CakeBakerScope>(
    decimalMode = DecimalMode(
        decimalPrecision = 200L,
        roundingMode = RoundingMode.FLOOR,
        scale = 10L
    )
) {
    lateinit var dataActions: DataActions

    override fun createScope(scopeType: ScopeType): CakeBakerScope = CakeBakerScope(scopeType, dataActions)

    fun initialize(uiViewModel: UIViewModel) {
        registerStandardFunctions()
        registerHelpers()
        registerUIActions(uiViewModel)

        dumpFunctionsToFile(this)

        val functionDump = getAllFunctions().joinToString("\n\n") { describeFunction(it) }
        logger.logInfo("Welcome to JsonMath 1.0.8")
        logger.logDebug("List of all available functions:\n$functionDump")
    }
}