package com.example.bluetoothchat.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String? = null,
    val voiceBase64: String? = null,
    val isFromMe: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)