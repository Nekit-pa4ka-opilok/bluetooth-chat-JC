package com.example.bluetoothchat.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bluetoothchat.Model.Chat
import com.example.bluetoothchat.ViewModel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chat: Chat,
    onBackClick: () -> Unit
) {
    val viewModel: ChatViewModel = viewModel()
    val messages by remember { derivedStateOf { viewModel.messages } }
    var messageText by remember { mutableStateOf("") }

    // Загружаем чат при входе
    LaunchedEffect(chat) {
        viewModel.loadChat(chat)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(chat.textName, fontWeight = FontWeight.SemiBold)
                            Text("В сети", fontSize = 12.sp, color = Color(0xFF4CAF50))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF373741),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF6A6D7A))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    ChatBubble(message)
                }
            }

            // Поле ввода сообщения
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color(0xFF373741), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Напишите сообщение...") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                IconButton(
                    onClick = {
                        viewModel.sendMessage(messageText)
                        messageText = ""
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Отправить", tint = Color(0xFF4CAF50))
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: com.example.bluetoothchat.ViewModel.Message) {
    val isMe = message.isFromMe
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val color = if (isMe) Color(0xFF4CAF50) else Color(0xFF424242)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(color)
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(text = message.text, color = Color.White, fontSize = 16.sp)
        }

        Text(
            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp),
            fontSize = 10.sp,
            color = Color.White.copy(0.6f),
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}