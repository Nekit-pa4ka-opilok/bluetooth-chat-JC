package com.example.bluetoothchat.View.Screens

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bluetoothchat.viewmodel.BluetoothViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(
    onDeviceSelected: (BluetoothDevice) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: BluetoothViewModel = viewModel()
    val devices by viewModel.devices.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val context = LocalContext.current

    // Состояние для хранения имен устройств
    val deviceNames = remember { mutableStateMapOf<String, String>() }

    // Запрашиваем разрешения
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            viewModel.initBluetooth(context)
            viewModel.observeService()
        }
    }

    LaunchedEffect(Unit) {
        // Проверка и запрос разрешений для Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val requiredPermissions = listOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE
            )
            val needRequest = requiredPermissions.any {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            }
            if (needRequest) {
                permissionsLauncher.launch(requiredPermissions.toTypedArray())
            } else {
                viewModel.initBluetooth(context)
                viewModel.observeService()
            }
        } else {
            viewModel.initBluetooth(context)
            viewModel.observeService()
        }
    }

    // Функция для безопасного получения имени устройства
    fun getDeviceName(device: BluetoothDevice): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                device.name ?: device.address.takeLast(4)
            } else {
                device.address.takeLast(4)
            }
        } else {
            device.name ?: device.address.takeLast(4)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Bluetooth устройства",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isScanning) {
                                viewModel.stopDiscovery()
                            } else {
                                viewModel.startDiscovery()
                            }
                        }
                    ) {
                        Icon(
                            if (isScanning) Icons.Default.BluetoothSearching else Icons.Default.Refresh,
                            "Обновить",
                            tint = if (isScanning) Color(0xFF4CAF50) else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF373741),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6A6D7A))
                .padding(padding)
        ) {
            // Статус подключения
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF373741)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Статус",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = when (connectionState) {
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Disconnected -> "Не подключен"
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connecting -> "Подключение..."
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connected -> {
                                    val state = connectionState as com.example.bluetoothchat.viewmodel.ConnectionState.Connected
                                    "Подключен к ${state.deviceName}"
                                }
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Error -> {
                                    val state = connectionState as com.example.bluetoothchat.viewmodel.ConnectionState.Error
                                    "Ошибка: ${state.message}"
                                }
                            },
                            fontSize = 14.sp,
                            color = when (connectionState) {
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connected -> Color(0xFF4CAF50)
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Connecting -> Color(0xFFFFC107)
                                is com.example.bluetoothchat.viewmodel.ConnectionState.Error -> Color(0xFFF44336)
                                else -> Color.White
                            },
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (connectionState is com.example.bluetoothchat.viewmodel.ConnectionState.Connected) {
                        TextButton(
                            onClick = { viewModel.disconnect() }
                        ) {
                            Text("Отключить", color = Color(0xFFF44336))
                        }
                    }
                }
            }

            // Список устройств
            if (devices.isEmpty() && !isScanning) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Bluetooth,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Нажмите на кнопку обновления\nдля поиска устройств",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(devices) { device ->
                        DeviceItem(
                            deviceName = getDeviceName(device),
                            deviceAddress = device.address,
                            onClick = { onDeviceSelected(device) }
                        )
                    }

                    if (isScanning) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        color = Color(0xFF4CAF50)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Поиск устройств...",
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceItem(
    deviceName: String,
    deviceAddress: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF373741)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Bluetooth,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = if (deviceName.isNotBlank()) deviceName else "Неизвестное устройство",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = deviceAddress,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}