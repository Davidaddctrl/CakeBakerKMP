package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.davidlukash.cakebaker.currentLocalTime
import com.davidlukash.cakebaker.debugTimestampFormat
import com.davidlukash.cakebaker.horizontalDragCursor
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.verticalDragCursor
import com.davidlukash.cakebaker.viewmodel.CakeBakerScope
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.jsonmath.engine.basic.OriginNode
import com.davidlukash.jsonmath.engine.basic.toTraceString
import com.davidlukash.jsonmath.engine.normal.EnumScopeType
import com.davidlukash.jsonmath.engine.normal.LanguageException
import com.davidlukash.jsonmath.engine.normal.ScopeType
import kotlinx.datetime.LocalTime
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

val background = Color(0xFF3D3D3D)
val surface = Color(0xFF262626)
val surface2 = Color(0xFF0A0A0A)
val red = Color.Red
val green = Color.Green
val textColor = Color.White

data class Log @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class) constructor(
    val message: String,
    val color: Color,
    val timestamp: LocalTime = currentLocalTime(),
    val uuid: Uuid = Uuid.random()
)

@Composable
fun DebugSideBar() {
    var width by remember { mutableStateOf(256.dp) }
    Box {
        Spacer(
            modifier = Modifier.fillMaxHeight().width(8.dp).draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    width -= delta.dp
                }
            ).horizontalDragCursor()
        )
        DebugPanel(
            modifier = Modifier.width(width).fillMaxHeight(),
        )
    }
}

@Composable
fun DebugPopup() {
    var width by remember { mutableStateOf(512.dp) }
    var height by remember { mutableStateOf(384.dp) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    Popup(
        offset = IntOffset(
            offset.x.roundToInt(),
            offset.y.roundToInt()
        )
    ) {
        Box {
            Spacer(
                modifier = Modifier.height(height).width(8.dp).draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        width -= delta.dp
                        offset = offset.copy(
                            x = offset.x + delta
                        )
                    }
                ).horizontalDragCursor().align(Alignment.TopStart)
            )
            Spacer(
                modifier = Modifier.height(height).width(8.dp).draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        width += delta.dp
                    }
                ).horizontalDragCursor().align(Alignment.TopEnd)
            )
            Spacer(
                modifier = Modifier.width(width).height(8.dp).draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        height += delta.dp
                    }
                ).verticalDragCursor().align(Alignment.BottomStart)
            )
            Spacer(
                modifier = Modifier.width(width).height(32.dp).draggable2D(
                    state = rememberDraggable2DState { deltaOffset ->
                        offset = offset.copy(
                            x = offset.x + deltaOffset.x,
                            y = offset.y + deltaOffset.y
                        )
                    }
                ).pointerHoverIcon(
                    PointerIcon.Hand
                ).align(Alignment.TopStart)
            )
            DebugPanel(
                modifier = Modifier.size(width, height).clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun DebugPanel(modifier: Modifier) {

    val mainViewModel = LocalMainViewModel.current
    val engine = mainViewModel.engine
    var logs by remember { mutableStateOf(listOf<Log>()) }
    var input by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val globalScope = mainViewModel.globalScope
    val localScope = remember { CakeBakerScope(ScopeType(EnumScopeType.LOCAL), mainViewModel.dataViewModel) }
    val origin = remember { OriginNode("Debug Console") }

    LaunchedEffect(logs) {
        if (logs.isNotEmpty()) lazyListState.scrollToItem(logs.size - 1)
    }
    Column(
        modifier = modifier.background(color = background).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Debug Console",
            fontFamily = FontFamily.Monospace,
            color = textColor,
            fontSize = 20.sp
        )
        Surface(
            color = surface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(2f).fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = lazyListState
            ) {
                items(
                    logs.size,
                    key = { logs[it].uuid }
                ) { index ->
                    val log = logs[index]
                    SelectionContainer {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontFamily = FontFamily.Monospace)) {
                                    withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.2f))) {
                                        append(debugTimestampFormat.format(log.timestamp))
                                        append(" ")
                                    }
                                    withStyle(style = SpanStyle(color = log.color)) {
                                        append(log.message)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            BasicTextField(
                value = input,
                onValueChange = { input = it },
                textStyle = TextStyle(fontFamily = FontFamily.Monospace, color = textColor),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.background(color = surface, shape = RoundedCornerShape(8.dp))
                            .fillMaxSize().padding(8.dp).fillMaxSize()
                    ) {
                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxSize(),
                cursorBrush = SolidColor(textColor),
            )
            Button(
                onClick = {
                    logs = logs + Log(input, textColor)
                    try {
                        val output = engine.evaluateExpression(
                            json.decodeFromString(input),
                            listOf(globalScope, localScope),
                            listOf(origin)
                        )
                        logs = logs + Log("Result: $output", green)
                    } catch (e: LanguageException) {
                        logs = logs + Log(e.toString() + e.origins?.toTraceString(), red)
                    } catch (e: Exception) {
                        logs = logs + Log(e.toString() + e.stackTraceToString(), red)
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd).zIndex(2f).offset(x = (-4).dp, y = (-4).dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = surface2,
                    contentColor = textColor
                )
            ) {
                Text("Enter", color = textColor, fontFamily = FontFamily.Monospace)
            }
        }
    }
}