package com.example.bluetoothchat.View.Screens

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluetoothchat.R
import com.example.bluetoothchat.View.Elements.AboutDialog
import com.example.bluetoothchat.View.Elements.SettingsItem
import com.example.bluetoothchat.View.Elements.SettingsSection
import com.example.bluetoothchat.utils.LocaleHelper
import java.util.Locale
import androidx.core.content.edit

@Preview(showBackground = true)
@Composable
fun SettingsScreen() {


    val context = LocalContext.current
    val showAboutDialog = remember { mutableStateOf(false) }
    val showLanguageDialog = remember { mutableStateOf(false) }

    var currentLangCode by remember {
        mutableStateOf(Locale.getDefault().language)
    }

    val currentLanguageDisplay = if (currentLangCode == "ru") "Русский" else "English"

    val bluetoothManager = remember { context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    val bluetoothAdapter = remember { bluetoothManager.adapter }

    var isBluetoothEnabled by remember { mutableStateOf(bluetoothAdapter?.isEnabled == true) }
    Surface(
        color = Color(0xFF6A6D7A),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SettingsSection(title = stringResource(R.string.section_main)) {
                SettingsItem(
                    icon = Icons.Default.Bluetooth,
                    title = stringResource(R.string.bluetooth),
                    subtitle = stringResource(R.string.bluetooth_status),
                    showSwitch = false,  // Убираем переключатель
                    onClick = {
                        if (bluetoothAdapter == null) {
                            context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                            return@SettingsItem
                        }

                        if (!isBluetoothEnabled) {
                            // Включаем Bluetooth
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                // Android 12+ нужна проверка разрешения
                                try {
                                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                    context.startActivity(intent)
                                } catch (_: SecurityException) {
                                    context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                                }
                            }
                        }
                    }
                )
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = stringResource(R.string.notifications),
                    subtitle = stringResource(R.string.notifications_subtitle),
                    showSwitch = true,
                    checked = true,
                    onCheckedChange = { }
                )
                SettingsItem(
                    icon = Icons.Default.Visibility,
                    title = stringResource(R.string.privacy),
                    subtitle = stringResource(R.string.privacy_subtitle),
                    onClick = { }
                )
            }

            SettingsSection(title = stringResource(R.string.section_appearance)) {
                SettingsItem(
                    icon = Icons.Default.DarkMode,
                    title = stringResource(R.string.dark_theme),
                    subtitle = stringResource(R.string.dark_theme_subtitle),
                )
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.language),
                    subtitle = currentLanguageDisplay,
                    onClick = { showLanguageDialog.value = true }
                )
            }

            SettingsSection(title = stringResource(R.string.section_about)) {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.about_greentooth),
                    subtitle = stringResource(R.string.version),
                    onClick = { showAboutDialog.value = true }
                )
                SettingsItem(
                    icon = Icons.Default.Share,
                    title = stringResource(R.string.share_app),
                    onClick = { }
                )
            }
        }
    }

    if (showAboutDialog.value) {
        AboutDialog(onDismiss = { showAboutDialog.value = false })
    }

    if (showLanguageDialog.value) {
        LanguageSelectionDialog(
            currentLangCode = currentLangCode,
            onLanguageSelected = { newLangCode ->
                context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
                    .edit {
                        putString("language", newLangCode)
                    }

                LocaleHelper.setLocale(context, newLangCode)
                (context as? android.app.Activity)?.recreate()
            },
            onDismiss = { showLanguageDialog.value = false }
        )
    }
}

@Composable
private fun LanguageSelectionDialog(
    currentLangCode: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val russian = stringResource(R.string.russian)
    val english = stringResource(R.string.english)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.choose_language),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                LanguageOption(
                    language = russian,
                    isSelected = currentLangCode == "ru",
                    onClick = { onLanguageSelected("ru") }
                )
                LanguageOption(
                    language = english,
                    isSelected = currentLangCode == "en",
                    onClick = { onLanguageSelected("en") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = Color.White)
            }
        },
        containerColor = Color(0xFF373741),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}

@Composable
private fun LanguageOption(
    language: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4CAF50)
            )
        }
    }
}

