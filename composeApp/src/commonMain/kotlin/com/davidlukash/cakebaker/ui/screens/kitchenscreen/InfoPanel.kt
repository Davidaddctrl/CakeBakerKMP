package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.LargePrimaryContainer
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.input.SwitchButton
import com.davidlukash.jsonmath.createObject
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InfoPanel(
    uiState: UIState, setAutoOvenEnabled: (Boolean) -> Unit,
    setAutoOrderCompleteEnabled: (Boolean) -> Unit, cutOutSize: Size
) {
    val satisfaction = uiState.customerSatisfaction
    val satisfactionLevel by remember(satisfaction) { derivedStateOf { uiState.getSatisfactionLevel() } }
    val currentCakeTier = uiState.currentCakeTier
    val cakesSalePrices by remember(uiState.items) { derivedStateOf { uiState.getCakesSalesPrices() } }
    val autoOven by remember(uiState.upgrades) { derivedStateOf { uiState.getAutoOven() } }
    val autoOrderComplete by remember(uiState.upgrades) { derivedStateOf { uiState.getAutoOrderComplete() } }
    val density = LocalDensity.current
    LargePrimaryContainer(
        modifier = Modifier.fillMaxWidth(),
        shapeOverrideFactory = { ShapeWithCutOut(cutOutSize, it, density) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                Theme.getString("title.information"),
                style = Theme.Styles.smallBodyStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    Theme.getString("label.customer_satisfaction"),
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ResourceImage(
                        data = when (satisfactionLevel) {
                            1 -> Theme.getImage("image.face.sad")
                            2 -> Theme.getImage("image.face.neutral_sad")
                            3 -> Theme.getImage("image.face.neutral")
                            4 -> Theme.getImage("image.face.medium")
                            5 -> Theme.getImage("image.face.happy")
                            else -> Theme.getImage("image.missing")
                        },
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(36.dp),
                    )
                    Text(
                        "$satisfaction%",
                        style = Theme.Styles.smallBodyStyle,
                    )
                }
            }
            Text(
                Theme.getString("label.cake_sale_price"),
                style = Theme.Styles.smallBodyStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$${toEngNotation(cakesSalePrices[currentCakeTier] ?: BigDecimal.ZERO)}",
                style = Theme.Styles.largeBodyStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            autoOven?.let { autoOven ->
                Text(
                    Theme.getString("label.auto_oven"),
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                SwitchButton(
                    value = autoOven.second,
                    onText = Theme.getString("action.on"),
                    offText = Theme.getString("action.off"),
                    enabled = autoOven.first,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    setAutoOvenEnabled(it)
                }
            }

            autoOrderComplete?.let { autoOrderComplete ->
                Text(
                    Theme.getString("label.auto_order_complete"),
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                SwitchButton(
                    value = autoOrderComplete.second,
                    onText = Theme.getString("action.on"),
                    offText = Theme.getString("action.off"),
                    enabled = autoOrderComplete.first,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    setAutoOrderCompleteEnabled(it)
                }
            }
        }
    }
}

class ShapeWithCutOut(val cutOutSize: Size, val radius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val cutW = cutOutSize.width
        val cutH = cutOutSize.height
        return Outline.Generic(
            Path().apply {
                moveTo(radius, 0f)
                lineTo(w - radius, 0f)
                arcTo(
                    rect = Rect(
                        w - radius * 2,
                        0f,
                        w,
                        radius * 2
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                lineTo(w, h - cutH - radius)
                arcTo(
                    rect = Rect(
                        w - radius * 2,
                        h - cutH - radius * 2,
                        w,
                        h - cutH
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                lineTo(w - cutW + radius, h - cutH)
                arcTo(
                    rect = Rect(
                        w - cutW,
                        h - cutH,
                        w - cutW + radius * 2,
                        h - cutH + radius * 2
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = -90f,
                    forceMoveTo = false
                )

                lineTo(w - cutW, h - radius)
                arcTo(
                    rect = Rect(
                        w - cutW - radius * 2,
                        h - radius * 2,
                        w - cutW,
                        h
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                lineTo(radius, h)
                arcTo(
                    rect = Rect(
                        0f,
                        h - radius * 2,
                        radius * 2,
                        h
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                lineTo(0f, radius)

                arcTo(
                    rect = Rect(
                        0f,
                        0f,
                        radius * 2,
                        radius * 2
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }
        )
    }

    constructor(cutOutSize: Size, radius: Dp, density: Density) : this(cutOutSize, density.run { radius.toPx() })
}

@Preview
@Composable
fun InfoPanelPreview() {
    var autoOvenEnabled by remember { mutableStateOf(true) }
    var autoOrderCompleteEnabled by remember { mutableStateOf(true) }
    val uiState = remember(autoOrderCompleteEnabled, autoOvenEnabled) { Save.state.copy(
        customerSatisfaction = 50,
        upgrades = Save.default.upgrades.filter { it.id == "upgrade.auto_oven" || it.name == "upgrade.auto_order_c-mplete" }.map {
            (if (it.id == "upgrade.auto_oven")
                it.copy(parameters = mapOf("enabled" to createObject(autoOvenEnabled)))
            else
                it.copy(parameters = mapOf("enabled" to createObject(autoOrderCompleteEnabled)))).copy(level = 1)
        },
    ) }
    Row(
        modifier = Modifier.size(400.dp, 720.dp)
    ) {
        InfoPanel(
            uiState = uiState,
            setAutoOvenEnabled = { autoOvenEnabled = it },
            setAutoOrderCompleteEnabled = { autoOrderCompleteEnabled = it },
            cutOutSize = Size(512f, 512f)
        )
    }
}