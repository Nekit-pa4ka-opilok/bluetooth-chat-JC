package com.example.bluetoothchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import com.example.bluetoothchat.Model.chats
import com.example.bluetoothchat.View.Elements.BottomScreenButton
import com.example.bluetoothchat.View.Elements.ShowLabel
import com.example.bluetoothchat.View.Screens.ProfileActivity
import com.example.bluetoothchat.View.Screens.SettingsActivity
import com.example.bluetoothchat.ui.theme.BluetoothChatTheme

private class Screen(
    val name: String,
    val activityClass: Class<out Activity>
)
private val screens = listOf(
    Screen("Chats", MainActivity::class.java),
    Screen("Settings", SettingsActivity::class.java),
    Screen("Profile", ProfileActivity::class.java)
)

private data object ChatsRoute
private data object SettingsRoute
private data object ProfileRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BluetoothChatTheme {
                Content()
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun Content() {
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = {
                        Text(
                            "Greentooth", fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "add dialog",
                                Modifier.size(33.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF373741),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                Row(
                    modifier = Modifier
                        .padding(1.dp)
                        .wrapContentSize()
                ) {
                    BottomScreenButton(
                        text = "Chats",
                        icon = Icons.Default.Email,
                        topStart = 26.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 26.dp,
                        cD = "чаты",
                        onClick = dropUnlessResumed {
                            screens[0].start()
                        }
                    )
                    BottomScreenButton(
                        text = "Settings",
                        icon = Icons.Default.Settings,
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp,
                        cD = "настройки",
                        onClick = dropUnlessResumed {
                            screens[1].start()
                        }
                    )
                    BottomScreenButton(
                        text = "Profile",
                        icon = Icons.Filled.AccountCircle,
                        topStart = 0.dp,
                        topEnd = 26.dp,
                        bottomEnd = 26.dp,
                        bottomStart = 0.dp,
                        cD = "профиль",
                        onClick = dropUnlessResumed {
                            screens[2].start()
                        }
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        )
        { innerPadding ->
            Surface(
                color = Color(0xFF6A6D7A),
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    LazyColumn() {
                        items(chats) { chat ->
                            ShowLabel(chat.avaResourse, chat.textName, chat.textLastMessage)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    1f to Color.Black.copy(alpha = 0.3f)
                                )
                            )
                    )
                }
            }
        }
    }

    private fun Screen.start() {
        val intent = Intent(this@MainActivity, this.activityClass)
        startActivity(intent)
    }
}