package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.json.JsonButtonValues
import com.davidlukash.cakebaker.data.theme.json.JsonContainerValues

data class ContainerValues(
    val elevation: Dp,
    val borderRadius: Dp,
    val borderWidth: Dp,
    val contentPadding: Dp,
) {
    fun toJsonValues(): JsonContainerValues = JsonContainerValues(
        elevation = elevation.value,
        borderRadius = borderRadius.value,
        borderWidth = borderWidth.value,
        contentPadding = contentPadding.value,
    )
}
