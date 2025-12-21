package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.davidlukash.cakebaker.currentLocalTime
import com.davidlukash.cakebaker.debugTimestampFormat
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.jsonmath.Expression
import kotlinx.datetime.LocalTime
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

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun RowScope.DebugConsole() {
    val mainViewModel = LocalMainViewModel.current
    val engine = mainViewModel.engine
    var logs by remember { mutableStateOf(listOf<Log>()) }
    var input by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(logs) {
        lazyListState.scrollToItem(logs.size - 1)
    }

    Column(
        modifier = Modifier.weight(1f).background(color = background).fillMaxSize().padding(16.dp),
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
            modifier = Modifier.weight(4f).fillMaxSize()
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
                        val output = engine.evaluateGeneric(
                            json.decodeFromString(input)
                        )
                        logs = logs + Log("Result: ${output.toPlainString()}", green)
                    } catch (e: Exception) {
                        logs = logs + Log(e.toString(), red)
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