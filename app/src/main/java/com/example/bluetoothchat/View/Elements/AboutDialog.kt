package com.example.bluetoothchat.View.Elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
public fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Greentooth",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Современное приложение для Bluetooth-чата\n\n" +
                            "• Быстрый обмен сообщениями\n" +
                            "• Надёжное соединение\n" +
                            "• Простой и понятный интерфейс\n\n" +
                            "Версия: 1.0.0\n" +
                            "Сделано для удобного общения",
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть", color = Color.White)
            }
        },
        containerColor = Color(0xFF373741),
        titleContentColor = Color.White,
        textContentColor = Color.White.copy(alpha = 0.9f)
    )
}