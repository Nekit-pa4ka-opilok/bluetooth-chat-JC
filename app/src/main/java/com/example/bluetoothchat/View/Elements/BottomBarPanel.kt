package com.example.bluetoothchat.View.Elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bluetoothchat.navigation.BottomScreen

@Composable
fun BottomBarPanel(navController: NavHostController) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        BottomScreenButton(
            text = "Chats",
            icon = Icons.Default.Email,
            topStart = 26.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 26.dp,
            cD = "чаты",
            onClick = {
                navController.navigate(BottomScreen.Chats.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            }
        )

        BottomScreenButton(
            text = "Settings",
            icon = Icons.Default.Settings,
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp,
            cD = "настройки",
            onClick = {
                navController.navigate(BottomScreen.Settings.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            }
        )

        BottomScreenButton(
            text = "Profile",
            icon = Icons.Filled.AccountCircle,
            topStart = 0.dp,
            topEnd = 26.dp,
            bottomEnd = 26.dp,
            bottomStart = 0.dp,
            cD = "профиль",
            onClick = {
                navController.navigate(BottomScreen.Profile.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            }
        )
    }
}
