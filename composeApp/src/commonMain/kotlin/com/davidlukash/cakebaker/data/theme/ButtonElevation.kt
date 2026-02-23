package com.davidlukash.cakebaker.data.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.animateElevation

data class ButtonElevation(
    val defaultElevation: Dp,
    val pressedElevation: Dp,
    val focusedElevation: Dp,
    val hoveredElevation: Dp,
    val disabledElevation: Dp,
) {
    @Composable
    fun animateElevation(
        enabled: Boolean,
        interactionSource: InteractionSource,
    ): State<Dp> {
        val interactions = remember { mutableStateListOf<Interaction>() }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> {
                        interactions.add(interaction)
                    }

                    is HoverInteraction.Exit -> {
                        interactions.remove(interaction.enter)
                    }

                    is FocusInteraction.Focus -> {
                        interactions.add(interaction)
                    }

                    is FocusInteraction.Unfocus -> {
                        interactions.remove(interaction.focus)
                    }

                    is PressInteraction.Press -> {
                        interactions.add(interaction)
                    }

                    is PressInteraction.Release -> {
                        interactions.remove(interaction.press)
                    }

                    is PressInteraction.Cancel -> {
                        interactions.remove(interaction.press)
                    }
                }
            }
        }

        val interaction = interactions.lastOrNull()

        val target =
            if (!enabled) {
                disabledElevation
            } else {
                when (interaction) {
                    is PressInteraction.Press -> pressedElevation
                    is HoverInteraction.Enter -> hoveredElevation
                    is FocusInteraction.Focus -> focusedElevation
                    else -> defaultElevation
                }
            }

        val animatable = remember { Animatable(target, Dp.VectorConverter) }

        LaunchedEffect(target) {
            if (animatable.targetValue != target) {
                if (!enabled) {
                    // No transition when moving to a disabled state
                    animatable.snapTo(target)
                } else {
                    val lastInteraction =
                        when (animatable.targetValue) {
                            pressedElevation -> PressInteraction.Press(Offset.Zero)
                            hoveredElevation -> HoverInteraction.Enter()
                            focusedElevation -> FocusInteraction.Focus()
                            else -> null
                        }
                    animatable.animateElevation(
                        from = lastInteraction,
                        to = interaction,
                        target = target,
                    )
                }
            }
        }

        return animatable.asState()
    }
}