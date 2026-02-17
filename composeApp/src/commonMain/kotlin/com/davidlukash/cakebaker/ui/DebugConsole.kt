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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.add
import cakebaker.composeapp.generated.resources.remove_drawable
import com.davidlukash.cakebaker.data.DataActions
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.data.log.LogType
import com.davidlukash.cakebaker.debugTimestampFormat
import com.davidlukash.cakebaker.horizontalDragCursor
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.verticalDragCursor
import com.davidlukash.cakebaker.engine.CakeBakerScope
import com.davidlukash.cakebaker.logger
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.withResult
import com.davidlukash.jsonmath.buildExpression
import com.davidlukash.jsonmath.data.Expression
import com.davidlukash.jsonmath.engine.basic.OriginNode
import com.davidlukash.jsonmath.engine.normal.EnumScopeType
import com.davidlukash.jsonmath.engine.normal.ScopeType
import com.davidlukash.jsonmath.engine.normal.VariableDescriptor
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

val background = Color(0xFF3D3D3D)
val surface = Color(0xFF262626)
val surface2 = Color(0xFF0A0A0A)
val red = Color.Red
val textColor = Color.White

val orange = Color(255, 127, 0)


@Composable
fun DebugSideBar() {
    var width by remember { mutableStateOf(256.dp) }
    val density = LocalDensity.current
    Box {
        Spacer(
            modifier = Modifier.fillMaxHeight().width(8.dp).draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    width -= density.run { delta.toDp() }
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
    var isHidden by remember { mutableStateOf(false) }
    DraggableResizablePopup(resizable = !isHidden) { (width, height) ->
        DebugPanel(
            modifier = Modifier.width(width).clip(RoundedCornerShape(8.dp)).then(
                if (isHidden) Modifier else Modifier.height(height)
            ),
            canHide = true,
            isHidden = isHidden,
        ) { isHidden = it }
    }
}

@Composable
fun DraggableResizablePopup(resizable: Boolean = true, content: @Composable (DpSize) -> Unit) {
    var width by remember { mutableStateOf(768.dp) }
    var height by remember { mutableStateOf(384.dp) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier.offset {
            IntOffset(
                offset.x.roundToInt(),
                offset.y.roundToInt()
            )
        }
    ) {
        if (resizable) {
            Spacer(
                modifier = Modifier.height(height).width(8.dp).draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        width -= density.run { delta.toDp() }
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
                        width += density.run { delta.toDp() }
                    }
                ).horizontalDragCursor().align(Alignment.TopEnd)
            )
            Spacer(
                modifier = Modifier.width(width).height(8.dp).draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        height += density.run { delta.toDp() }
                    }
                ).verticalDragCursor().align(Alignment.BottomStart)
            )
        }
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
        content(DpSize(width = width, height = height))
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun DebugPanel(
    modifier: Modifier,
    canHide: Boolean = false,
    isHidden: Boolean = false,
    setHidden: (Boolean) -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val engine = mainViewModel.engine
    val uiViewModel = mainViewModel.uiViewModel
    val logs by uiViewModel.logs.collectAsState()
    val globalScope = mainViewModel.dataViewModel.globalScope
    val localScope = remember {
        CakeBakerScope(
            ScopeType(EnumScopeType.LOCAL),
            DataActions.fromDataViewModel(mainViewModel.dataViewModel)
        )
    }
    var descriptorNames by remember { mutableStateOf(globalScope.listVariables().map { it.name }) }
    DebugPanelContent(
        logs,
        execute = {
            logger.logDebug(it)
            try {
                val code = json.decodeFromString<Expression>(it)
                val output = engine.evaluateExpression(
                    code,
                    listOf(globalScope, localScope),
                    listOf(OriginNode("Debug Console", listOf(code)))
                )
                logger.logDebug("Result: $output")
            } catch (e: Exception) {
                logger.logError(e)
            }
        },
        modifier = modifier,
        canHide = canHide,
        isHidden = isHidden,
        setHidden = setHidden,
        refresh = { descriptorNames = globalScope.listVariables().map { it.name } },
        getDescriptor = { name -> globalScope.listVariables().find { it.name == name } },
        descriptorNames = descriptorNames,
    )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun DebugPanelContent(
    logs: List<Log>,
    execute: (String) -> Unit,
    refresh: () -> Unit = {},
    getDescriptor: (String) -> VariableDescriptor?,
    descriptorNames: List<String>,
    modifier: Modifier,
    canHide: Boolean = false,
    isHidden: Boolean = false,
    setHidden: (Boolean) -> Unit = {}
) {
    var input by remember { mutableStateOf("") }
    var isVariableView by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.background(color = background).padding(horizontal = 16.dp).padding(
            top = if (canHide) 8.dp else 16.dp,
            bottom = if (isHidden) 8.dp else 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                bottom = if (canHide) 0.dp else 8.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (isVariableView) "Variable View" else "Debug Console",
                fontFamily = FontFamily.Monospace,
                color = textColor,
                fontSize = 20.sp
            )
            if (canHide) {
                if (isHidden) {
                    IconButton(
                        onClick = { setHidden(false) },
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.add),
                            contentDescription = "Show",
                            tint = textColor
                        )
                    }
                } else {
                    IconButton(
                        onClick = { setHidden(true) },
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.remove_drawable),
                            contentDescription = "Hide",
                            tint = textColor
                        )
                    }
                }
            }
        }
        if (!isHidden) {
            if (isVariableView)
                VariableView(
                    refresh = refresh,
                    getDescriptor = getDescriptor,
                    descriptorNames = descriptorNames,
                )
            else
                DebugConsole(
                    logs = logs,
                    input = input,
                ) { input = it }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InputButton(
                    onClick = {
                        isVariableView = !isVariableView
                    },
                ) {
                    Text(if (isVariableView) "Open Console" else "Open Variable View")
                }
                if (!isVariableView) {
                    InputButton(
                        onClick = {
                            execute(input)
                        },
                    ) {
                        Text("Execute")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                InputButton(
                    onClick = {
                        val expression = buildExpression {
                            function {
                                name = "console.mode"
                                appendString("NONE")
                            }
                        }
                        execute(json.encodeToString(expression))
                    },
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ColumnScope.DebugConsole(
    logs: List<Log>,
    input: String,
    onInputChange: (String) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(logs) {
        if (logs.isNotEmpty()) lazyListState.scrollToItem(logs.size - 1)
    }
    Surface(
        color = surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f).fillMaxSize().padding(
            bottom = 8.dp
        )
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
                Log(log)
            }
        }
    }
    Box(
        modifier = Modifier.weight(1f)
    ) {
        InputField(
            input = input,
            modifier = Modifier.fillMaxSize()
        ) { onInputChange(it) }
    }
}

@Composable
fun ColumnScope.VariableView(
    refresh: () -> Unit = {},
    getDescriptor: (String) -> VariableDescriptor?,
    descriptorNames: List<String>,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    val filteredDescriptorNames by remember {
        derivedStateOf {
            if (searchText.isBlank()) return@derivedStateOf descriptorNames
            val tokens = searchText.split("/").filter { it.isNotBlank() }.map { it.trim(' ') }
            (descriptorNames.filter { descriptorName -> tokens.any { it == descriptorName } } +
                    descriptorNames.filter { descriptorName ->
                        tokens.all {
                            descriptorName.contains(it, ignoreCase = true)
                        }
                    }).distinct()
        }
    }
    var key by remember { mutableStateOf(0) }
    LaunchedEffect(searchText) {
        coroutineScope.launch {
            lazyListState.scrollToItem(0)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            "Search:",
            fontFamily = FontFamily.Monospace,
            color = textColor,
        )
        InputField(
            input = searchText,
            modifier = Modifier.weight(1f),
            maxLines = 1,
        ) { searchText = it }
        InputButton(
            onClick = {
                refresh()
                key++
            }
        ) { Text("Refresh") }
    }
    key(key) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyListState
        ) {
            items(filteredDescriptorNames, key = { it }) { descriptorName ->
                var value by remember {
                    mutableStateOf(
                        getDescriptor(descriptorName)?.get?.invoke()
                            ?.let { json.encodeToString(it) } ?: "Not Readable"
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(descriptorName, fontFamily = FontFamily.Monospace, color = textColor)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        InputField(
                            input = value,
                            enabled = getDescriptor(descriptorName)?.set != null,
                            modifier = Modifier.heightIn(48.dp, 512.dp).weight(1f)
                        ) { value = it }
                        if (getDescriptor(descriptorName)?.set != null) {
                            InputButton(
                                onClick = {
                                    withResult {
                                        getDescriptor(descriptorName)?.set?.invoke(json.decodeFromString(value))
                                    }
                                },
                                modifier = Modifier.width(96.dp)
                            ) { Text("Set") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Log(log: Log) {
    SelectionContainer {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = FontFamily.Monospace)) {
                    withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.2f))) {
                        append(debugTimestampFormat.format(log.timestamp))
                        append(" ")
                        if (log.logType == LogType.INFO || log.logType == LogType.WARN)
                            append(" ")
                        append("[")
                        append(log.logType.toString())
                        append("]")
                        append(" ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = when (log.logType) {
                                LogType.DEBUG -> textColor.copy(alpha = 0.5f)
                                LogType.INFO -> textColor
                                LogType.WARN -> orange
                                LogType.ERROR -> red
                            }
                        )
                    ) {
                        append(log.message)
                    }
                }
            }
        )
    }
}

@Composable
fun InputField(
    input: String,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = input,
        onValueChange = onValueChange,
        textStyle = TextStyle(fontFamily = FontFamily.Monospace, color = textColor),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.background(color = surface, shape = RoundedCornerShape(8.dp)).padding(8.dp)
            ) {
                innerTextField()
            }
        },
        modifier = modifier,
        cursorBrush = SolidColor(textColor),
        maxLines = maxLines,
        enabled = enabled
    )
}

@Composable
fun InputButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = surface2,
            contentColor = textColor
        )
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview(
    widthDp = 800,
    heightDp = 600
)
@Composable
fun DebugPanelPreview() {
    var logs by remember {
        mutableStateOf(
            listOf<Log>(
                Log("Debug Panel Preview Debug", LogType.DEBUG),
                Log("Debug Panel Preview Info", LogType.INFO),
                Log("Debug Panel Preview Warn", LogType.WARN),
                Log("Debug Panel Preview Error", LogType.ERROR),
            )
        )
    }
    var isHidden by remember { mutableStateOf(false) }
    DebugPanelContent(
        logs = logs,
        execute = {
            logs = logs + Log(it, LogType.DEBUG)
        },
        modifier = Modifier.size(800.dp, 600.dp),
        canHide = true,
        isHidden = isHidden,
        refresh = {  },
        getDescriptor = { null },
        descriptorNames = listOf(),
    ) { isHidden = it }
}