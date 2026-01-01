package com.davidlukash.cakebaker.viewmodel

import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.jsonmath.engine.normal.Engine
import com.davidlukash.jsonmath.engine.normal.ScopeType
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode

class CakeBakerEngine(
    val mainViewModel: MainViewModel
) : Engine<CakeBakerScope>(
    decimalMode = DecimalMode(
        decimalPrecision = 200L,
        roundingMode = RoundingMode.FLOOR,
        scale = 10L
    )
) {
    override fun createScope(scopeType: ScopeType): CakeBakerScope = CakeBakerScope(scopeType, mainViewModel.dataViewModel)
}