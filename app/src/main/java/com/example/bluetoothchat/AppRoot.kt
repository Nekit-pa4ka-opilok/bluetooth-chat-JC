package com.example.bluetoothchat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothchat.Model.chats
import com.example.bluetoothchat.View.Elements.BottomBarPanel
import com.example.bluetoothchat.View.Screens.ChatScreen
import com.example.bluetoothchat.View.Screens.ChatsScreen
import com.example.bluetoothchat.View.Screens.ProfileScreen
import com.example.bluetoothchat.View.Screens.SettingsScreen
import com.example.bluetoothchat.navigation.BottomScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Greentooth",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Добавить новый чат */ }) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "add dialog",
                            Modifier.size(33.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF373741),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            BottomBarPanel(navController = navController)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomScreen.Chats.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomScreen.Chats.route) {
                ChatsScreen(navController = navController)
            }

            composable(BottomScreen.Settings.route) {
                SettingsScreen()
            }

            composable(BottomScreen.Profile.route) {
                ProfileScreen()
            }

            // Новый маршрут для открытия чата
            composable("chat/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""

                // Находим чат по ID
                val selectedChat = chats.find { it.id == chatId } ?: chats.firstOrNull()

                selectedChat?.let { chat ->
                    ChatScreen(
                        chat = chat,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}