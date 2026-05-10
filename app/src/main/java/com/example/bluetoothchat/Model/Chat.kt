package com.example.bluetoothchat.Model

data class Chat(
    val id: String,
    val textName: String,
    val avaResourse: Int,           // R.drawable.avatar_...
    val textLastMessage: String,
    val lastMessageTime: String = "14:32",
    val unreadCount: Int = 0,       // Количество непрочитанных сообщений
    val isOnline: Boolean = false
)