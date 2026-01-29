package com.davidlukash.cakebaker.platformui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

sealed class HorizontalArrangement {
    class SpacedBy(val space: Dp, val alignment: Alignment.Horizontal = Alignment.Start) : HorizontalArrangement()
    class SpaceBetween : HorizontalArrangement()
    class SpaceEvenly : HorizontalArrangement()
    class SpaceAround : HorizontalArrangement()
    class Start : HorizontalArrangement()
    class Center : HorizontalArrangement()
    class End : HorizontalArrangement()

    fun toCompose(): Arrangement.Horizontal = when (this) {
            is SpacedBy -> Arrangement.spacedBy(space, alignment)
            is SpaceBetween -> Arrangement.SpaceBetween
            is SpaceEvenly -> Arrangement.SpaceEvenly
            is SpaceAround -> Arrangement.SpaceAround
            is Start -> Arrangement.Start
            is Center -> Arrangement.Center
            is End -> Arrangement.End
        }

    companion object {
        val SpaceBetween = SpaceBetween()
        val SpaceEvenly = SpaceEvenly()
        val SpaceAround = SpaceAround()
        val Start = Start()
        val Center = Center()
        val End = End()
    }
}