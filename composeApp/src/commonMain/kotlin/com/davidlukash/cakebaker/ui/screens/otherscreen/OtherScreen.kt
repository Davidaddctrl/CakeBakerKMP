package com.davidlukash.cakebaker.ui.screens.otherscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.coilkt_license
import cakebaker.composeapp.generated.resources.compose_license
import cakebaker.composeapp.generated.resources.kmp_bignum_license
import cakebaker.composeapp.generated.resources.kotlin_license
import cakebaker.composeapp.generated.resources.ktor_license
import cakebaker.composeapp.generated.resources.ktx_datetime_license
import cakebaker.composeapp.generated.resources.ktx_serialization_license
import cakebaker.composeapp.generated.resources.material_design_icons_license
import cakebaker.composeapp.generated.resources.mpl_license
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.LargeThemedButton
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.ViewModelProvided
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OtherScreen(theme: Theme, navigateWithFade: (Screen) -> Unit) {
    val licenses = remember {
        mapOf(
            "Cake Baker" to Res.string.mpl_license,
            "Json Math davidaddctrl" to Res.string.mpl_license,
            "Material Design Icons" to Res.string.material_design_icons_license,
            "Kotlin" to Res.string.kotlin_license,
            "Kotlinx Serialization" to Res.string.ktx_serialization_license,
            "Kotlinx Datetime" to Res.string.ktx_datetime_license,
            "Kotlin Multiplatform Bignum" to Res.string.kmp_bignum_license,
            "Compose Multiplatform" to Res.string.compose_license,
            "Coil" to Res.string.coilkt_license,
            "Ktor" to Res.string.ktor_license
        )
    }
    var currentLicense by remember { mutableStateOf<Pair<String, StringResource?>?>(null) }

    val trueDensity by if (ViewModelProvided.current) {
        LocalMainViewModel.current.uiViewModel.trueDensity.collectAsState()
    } else {
        val density = LocalDensity.current
        derivedStateOf { mutableStateOf(density) }
    }
    if (currentLicense != null) {
        CompositionLocalProvider(
            LocalDensity provides (trueDensity as? Density ?: LocalDensity.current),
        ) {
            AlertDialog(
                onDismissRequest = {
                    currentLicense = null
                },
                confirmButton = {
                    TextButton(
                        onClick = { currentLicense = null }
                    ) {
                        Text("Dismiss")
                    }
                },
                title = {
                    Text(
                        "License for ${currentLicense?.first}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                modifier = Modifier.fillMaxSize(),
                text = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        SelectionContainer(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                        ) {
                            Text(
                                text = currentLicense?.second?.let { stringResource(it) }.toString(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            )
                        }
                    }
                }
            )
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(theme)
        },
        bottomBar = {
            BottomBar(theme, navigateWithFade)
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(320.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            items(licenses.keys.toList()) { key ->
                LargeThemedButton(
                    theme = theme,
                    onClick = { currentLicense = key to licenses[key] },
                    modifier = Modifier.width(320.dp).height(180.dp)
                ) {
                    Text(key, style = theme.subtitleStyle, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview(
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun OtherScreenPreview() {
    val theme = getDefaultTheme()
    Box(Modifier.fillMaxSize().background(theme.backgroundColor).padding(8.dp)) {
        OtherScreen(theme = theme) { }
    }
}