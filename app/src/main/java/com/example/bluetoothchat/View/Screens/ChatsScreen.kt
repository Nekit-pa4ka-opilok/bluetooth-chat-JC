package com.example.bluetoothchat.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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

    Scaffold(
        modifier = Modifier.background(color = Color(0xFF6A6D7A))
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

@Preview(showBackground = true)
@Composable
fun PreviewChatsScreen(){
    ChatsScreen(navController = rememberNavController())
}