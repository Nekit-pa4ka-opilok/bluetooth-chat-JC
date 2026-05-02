package com.example.bluetoothchat.View.Elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomScreenButton(text: String,
                       icon: ImageVector,
                       topStart: Dp = 0.dp,
                       topEnd: Dp = 0.dp,
                       bottomEnd: Dp = 0.dp,
                       bottomStart: Dp = 0.dp,
                       cD: String,
                       onClick: () -> Unit
                       ) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF4A4E64),
        shape = RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomEnd = bottomEnd,
            bottomStart = bottomStart
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Icon(icon, cD, tint = Color.White)
            Text(text = text, color = Color.White, fontSize = 12.33.sp)
        }
    }
}
 @Preview(showBackground = true)
@Composable
 fun PreviewBottomScreenButton(){
     BottomScreenButton(
         text = "Chats",
         icon = Icons.Default.Email,
         topStart = 26.dp,
         topEnd = 0.dp,
         bottomEnd = 26.dp,
         bottomStart = 0.dp,
         cD = "чаты",
         onClick = {}
     )
 }