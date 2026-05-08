package com.example.bluetoothchat.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    Surface(
        color = Color(0xFF6A6D7A),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Круглый аватар с обводкой
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C35))
                    .border(5.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    fontSize = 80.sp
                )
                // Можно заменить на Image позже
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Имя пользователя
            Text(
                text = "Алексей Иванов",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Статус
            Text(
                text = "Онлайн",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Кнопка "Редактировать"
            Button(
                onClick = { /* TODO: Открыть редактирование профиля */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF373741)
                ),
                modifier = Modifier
                    .width(220.dp)
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Редактировать профиль",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Дополнительная информация (по желанию)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                ProfileInfoItem(title = "Устройства", value = "3 подключено")
                ProfileInfoItem(title = "Сообщений отправлено", value = "1248")
                ProfileInfoItem(title = "В сети", value = "12 дней")
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = Color.White.copy(alpha = 0.7f))
        Text(text = value, color = Color.White, fontWeight = FontWeight.Medium)
    }
}