package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun KitchenScreen(
    uiState: UIState,
    navigateWithFade: (Screen) -> Unit,
    bake: () -> Unit,
    setAutoOvenEnabled: (Boolean) -> Unit,
    setAutoOrderCompleteEnabled: (Boolean) -> Unit,
    completeOrder: (Order) -> Unit,
    setCurrentCake: (Int) -> Unit,
    ovenProgress: Double,
    ovenRunning: Boolean,
    nextOrderRemainingTime: Double?,
    orders: List<Order>,
) {
    Scaffold(
        topBar = {
            TopBar(uiState)
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        MainContent(
            uiState,
            navigateWithFade,
            bake,
            setAutoOvenEnabled,
            setAutoOrderCompleteEnabled,
            completeOrder,
            setCurrentCake,
            ovenProgress,
            ovenRunning,
            nextOrderRemainingTime,
            orders,
            innerPadding
        )
    }
}