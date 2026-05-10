package com.example.bluetoothchat.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.bluetoothchat.Model.Chat
import java.util.Date

data class Message(
    val id: String = System.currentTimeMillis().toString(),
    val text: String,
    val isFromMe: Boolean,
    val timestamp: Date = Date()
)

class ChatViewModel : ViewModel() {

    // Список сообщений для текущего чата
    val messages = mutableStateListOf<Message>()

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        messages.add(
            Message(
                text = text,
                isFromMe = true
            )
        )
    }

    // Можно добавить загрузку истории сообщений по chatId в будущем
    fun loadChat(chat: Chat) {
        messages.clear()
        // Здесь в будущем можно загрузить реальную историю
        messages.addAll(
            listOf(
                Message(text = "Привет! Как дела?", isFromMe = false),
                Message(text = "Нормально, а у тебя?", isFromMe = true),
            )
        )
    }
}