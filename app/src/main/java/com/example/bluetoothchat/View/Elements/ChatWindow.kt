package com.example.bluetoothchat.View.Elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluetoothchat.R

    @Composable
    fun ShowLabel(
        avaResourse: Int,
        textName: String,
        textLastMessage: String
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(70.dp)
                .background(color = Color.Black.copy(alpha = 0.3f))
        ){
            Image(painter = painterResource(id = avaResourse),
                contentDescription = "avatar",
                modifier = Modifier.padding(11.dp)
                    .clip(shape = CircleShape)
            )
            Column(verticalArrangement = Arrangement.Center){
                Text(text = textName,
                    fontSize = 15.sp,
                    style = TextStyle(color = Color.White, fontWeight =  FontWeight.Bold),
                    modifier = Modifier.padding(top = 11.dp)
                )
                Text(text = textLastMessage,
                    fontSize = 15.sp,
                    style = TextStyle(color = Color.White.copy(alpha = 0.68f)),
                    maxLines = 1,
                    modifier = Modifier.padding(top = 11.dp)
                )
            }
        }
    }
    @Preview(showBackground = true, backgroundColor = 0x475839)
    @Composable
    fun PreviewShowLabel(){
        ShowLabel(
            avaResourse = R.drawable.ava_profile,
            textName = "Name",
            textLastMessage = "last message1 last message2 last message3 last message4 last message5 last message6"
        )
    }