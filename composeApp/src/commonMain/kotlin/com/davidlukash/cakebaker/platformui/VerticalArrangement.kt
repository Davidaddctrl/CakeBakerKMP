package com.davidlukash.cakebaker.platformui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

sealed class VerticalArrangement {
    class SpacedBy(val space: Dp, val alignment: Alignment.Vertical = Alignment.Top) : VerticalArrangement()
    class SpaceBetween : VerticalArrangement()
    class SpaceEvenly : VerticalArrangement()
    class SpaceAround : VerticalArrangement()
    class Top : VerticalArrangement()
    class Center : VerticalArrangement()
    class Bottom : VerticalArrangement()

    fun toCompose(): Arrangement.Vertical = when (this) {
            is SpacedBy -> Arrangement.spacedBy(space, alignment)
            is SpaceBetween -> Arrangement.SpaceBetween
            is SpaceEvenly -> Arrangement.SpaceEvenly
            is SpaceAround -> Arrangement.SpaceAround
            is Top -> Arrangement.Top
            is Center -> Arrangement.Center
            is Bottom -> Arrangement.Bottom
        }

    companion object {
        val SpaceBetween = SpaceBetween()
        val SpaceEvenly = SpaceEvenly()
        val SpaceAround = SpaceAround()
        val Top = Top()
        val Center = Center()
        val Bottom = Bottom()
    }
}