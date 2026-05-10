package com.example.bluetoothchat.View.Elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluetoothchat.Model.Chat
import com.example.bluetoothchat.Model.chats
import com.example.bluetoothchat.R

@Composable
fun ShowLabel(
    chat: Chat,
    onChatClick: (Chat) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color.Black.copy(alpha = 0.3f))
            .clickable { onChatClick(chat) }
    ) {
        Image(
            painter = painterResource(id = chat.avaResourse),
            contentDescription = "avatar",
            modifier = Modifier
                .padding(11.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = chat.textName,
                fontSize = 15.sp,
                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
            )
            Text(
                text = chat.textLastMessage,
                fontSize = 15.sp,
                style = TextStyle(color = Color.White.copy(alpha = 0.68f)),
                maxLines = 1
            )
        }

        if (chat.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp, top = 8.dp)
                    .size(24.dp)
                    .background(Color(0xFF4CAF50), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.unreadCount.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShowLabel() {
    ShowLabel(
        chat = chats[1],
        onChatClick = {  }
    )
}