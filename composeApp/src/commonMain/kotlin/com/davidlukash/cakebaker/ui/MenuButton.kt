package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.chevron_backward
import cakebaker.composeapp.generated.resources.menu
import com.davidlukash.cakebaker.ui.navigation.MenuScreen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.ViewModelProvided
import org.jetbrains.compose.resources.painterResource

@Composable
fun MenuButton(content: @Composable () -> Unit) {
    val uiViewModel = if (!ViewModelProvided.current) null else LocalMainViewModel.current.uiViewModel
    Box(modifier = Modifier.fillMaxWidth()) {
        content()
        IconButton(
            onClick = {
                uiViewModel?.navigateWithFade(MenuScreen)
            },
            modifier = Modifier.align(Alignment.TopStart).size(72.dp).offset(x = 24.dp, y = 24.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.menu),
                contentDescription = "Menu",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }
    }
}