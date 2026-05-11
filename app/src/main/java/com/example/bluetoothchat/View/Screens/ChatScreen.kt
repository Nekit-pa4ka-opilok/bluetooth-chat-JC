package com.example.bluetoothchat.View.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bluetoothchat.Model.Chat
import com.example.bluetoothchat.model.ChatMessage
import com.example.bluetoothchat.viewmodel.BluetoothViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chat: Chat?,
    onBackClick: () -> Unit
) {
    val bluetoothViewModel: BluetoothViewModel = viewModel()
    val context = LocalContext.current
    val messages by bluetoothViewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val connectionState by bluetoothViewModel.connectionState.collectAsState()

    // Запрашиваем разрешения для Bluetooth
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            // Разрешения получены
        }
    }

    LaunchedEffect(Unit) {
        bluetoothViewModel.initBluetooth(context)
        bluetoothViewModel.observeService()

        // Запрос разрешений для Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val requiredPermissions = listOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.RECORD_AUDIO
            )
            val needRequest = requiredPermissions.any {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            }
            if (needRequest) {
                permissionsLauncher.launch(requiredPermissions.toTypedArray())
            }
        }
    }

    // Определяем название чата
    val chatTitle = if (chat != null) {
        chat.textName
    } else {
        when (connectionState) {
            is com.example.bluetoothchat.viewmodel.ConnectionState.Connected -> {
                (connectionState as com.example.bluetoothchat.viewmodel.ConnectionState.Connected).deviceName
            }
            else -> "Bluetooth чат"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            chatTitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = when (connectionState) {
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connected -> "Подключен"
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connecting -> "Подключение..."
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Disconnected -> "Не подключен"
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Error -> "Ошибка"
                            },
                            fontSize = 12.sp,
                            color = when (connectionState) {
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connected -> Color(0xFF4CAF50)
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connecting -> Color(0xFFFFC107)
                                else -> Color.Red
                            }
                        )
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
                ),
                actions = {
                    if (connectionState is com.example.bluetoothchat.viewmodel.ConnectionState.Disconnected) {
                        TextButton(
                            onClick = {
                                // TODO: Открыть список устройств
                                onBackClick()
                            }
                        ) {
                            Text("Подключить", color = Color(0xFF4CAF50))
                        }
                    }
                }
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
                    ChatBubbleMessage(message)
                }
            }

            // Поле ввода сообщения
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF373741)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = {
                            Text(
                                "Напишите сообщение...",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        enabled = connectionState is com.example.bluetoothchat.viewmodel.ConnectionState.Connected
                    )

                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                bluetoothViewModel.sendMessage(messageText)
                                messageText = ""
                            }
                        },
                        enabled = connectionState is com.example.bluetoothchat.viewmodel.ConnectionState.Connected
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Отправить",
                            tint = if (connectionState is com.example.bluetoothchat.viewmodel.ConnectionState.Connected)
                                Color(0xFF4CAF50)
                            else
                                Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleMessage(message: ChatMessage) {
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
            Text(
                text = message.text ?: (if (message.voiceBase64 != null) "🎤 Голосовое сообщение" else ""),
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Text(
            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = if (isMe) 0.dp else 8.dp, top = 2.dp)
        )
    }
}