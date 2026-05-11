package com.example.bluetoothchat.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothchat.Model.chats
import com.example.bluetoothchat.View.Elements.ShowLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(navController: NavHostController) {
    var showDeviceList by remember { mutableStateOf(false) }

    if (showDeviceList) {
        DeviceListScreen(
            onDeviceSelected = { device ->
                // Переходим в чат с выбранным устройством
                navController.navigate("chat/${device.address}") {
                    popUpTo("chats") { saveState = true }
                    launchSingleTop = true
                }
                showDeviceList = false
            },
            onBackClick = { showDeviceList = false }
        )
    } else {
        Scaffold(
            modifier = Modifier.background(color = Color(0xFF6A6D7A)),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDeviceList = true },
                    containerColor = Color(0xFF4CAF50)
                ) {
                    Icon(
                        Icons.Default.BluetoothSearching,
                        contentDescription = "Поиск устройств",
                        tint = Color.White
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(chats) { chat ->
                    ShowLabel(
                        chat = chat,
                        onChatClick = {
                            navController.navigate("chat/${chat.id}")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatsScreen() {
    ChatsScreen(navController = rememberNavController())
}