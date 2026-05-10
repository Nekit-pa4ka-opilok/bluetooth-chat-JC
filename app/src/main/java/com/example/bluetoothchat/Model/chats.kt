package com.example.bluetoothchat.Model

import com.example.bluetoothchat.R

val chats = listOf(
    Chat(
        id = "1",
        textName = "Дмитрий Соколов",
        avaResourse = R.drawable.ava_profile,
        textLastMessage = "Ок, договорились 👍",
        lastMessageTime = "14:32",
        unreadCount = 2,
        isOnline = true
    ),
    Chat(
        id = "2",
        textName = "Анна Морозова",
        avaResourse = R.drawable.ava_profile,
        textLastMessage = "Когда встретимся?",
        lastMessageTime = "12:05",
        unreadCount = 0,
        isOnline = false
    ),
    Chat(
        id = "3",
        textName = "Игорь Петров",
        avaResourse = R.drawable.ava_profile,
        textLastMessage = "Отправил файл",
        lastMessageTime = "09:17",
        unreadCount = 1,
        isOnline = true
    )
)