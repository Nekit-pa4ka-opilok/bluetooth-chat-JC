package com.example.bluetoothchat.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class BottomScreen(val route: String) {
    data object Chats : BottomScreen("chats")
    data object Settings : BottomScreen("settings")
    data object Profile : BottomScreen("profile")
}