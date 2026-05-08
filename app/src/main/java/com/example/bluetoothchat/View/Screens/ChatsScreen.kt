package com.example.bluetoothchat.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bluetoothchat.Model.chats
import com.example.bluetoothchat.View.Elements.ShowLabel

@Composable
fun ChatsScreen() {
    Surface(
        color = Color(0xFF6A6D7A),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            LazyColumn() {
                items(chats) { chat ->
                    ShowLabel(chat.avaResourse, chat.textName, chat.textLastMessage)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.3f)
                        )
                    )
            )
        }
    }
}