package com.example.bluetoothchat.View.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bluetoothchat.R
import java.io.File
import java.io.FileOutputStream

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    // Загрузка сохранённых данных
    var userName by remember { mutableStateOf(loadSavedName(context)) }
    var avatarUri by remember { mutableStateOf<Uri?>(loadSavedAvatarUri(context)) }

    val showEditNameDialog = remember { mutableStateOf(false) }

    // Лаунчер для выбора изображения из галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedUri = saveAvatarToInternalStorage(context, it)
            avatarUri = savedUri
        }
    }

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

            // Аватарка
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C35))
                    .border(5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri != null) {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "👤",
                        fontSize = 80.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = stringResource(R.string.online),
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { showEditNameDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF373741)),
                modifier = Modifier
                    .width(220.dp)
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.edit_profile))
            }

            Spacer(modifier = Modifier.height(32.dp))

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

    if (showEditNameDialog.value) {
        EditNameDialog(
            currentName = userName,
            onNameChanged = { newName ->
                userName = newName
                saveName(context, newName)
            },
            onDismiss = { showEditNameDialog.value = false }
        )
    }
}

// ==================== СОХРАНЕНИЕ ИМЕНИ ====================

private fun saveName(context: android.content.Context, name: String) {
    context.getSharedPreferences("profile_prefs", android.content.Context.MODE_PRIVATE)
        .edit()
        .putString("user_name", name)
        .apply()
}

private fun loadSavedName(context: android.content.Context): String {
    return context.getSharedPreferences("profile_prefs", android.content.Context.MODE_PRIVATE)
        .getString("user_name", "Алексей Иванов") ?: "Алексей Иванов"
}

// ==================== СОХРАНЕНИЕ АВАТАРКИ ====================

private fun saveAvatarToInternalStorage(context: android.content.Context, uri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, "profile_avatar.jpg")

        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun loadSavedAvatarUri(context: android.content.Context): Uri? {
    val file = File(context.filesDir, "profile_avatar.jpg")
    return if (file.exists()) Uri.fromFile(file) else null
}

// ==================== ДИАЛОГ ИЗМЕНЕНИЯ ИМЕНИ ====================

@Composable
private fun EditNameDialog(
    currentName: String,
    onNameChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Изменить имя", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Имя") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (newName.isNotBlank()) {
                    onNameChanged(newName)
                }
                onDismiss()
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        containerColor = Color(0xFF373741),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
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