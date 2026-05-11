package com.example.bluetoothchat.service

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.example.bluetoothchat.model.ChatMessage
import com.example.bluetoothchat.viewmodel.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothChatService : Service() {

    private val binder = LocalBinder()
    private var socket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    companion object {
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    inner class LocalBinder : Binder() {
        fun getService(): BluetoothChatService = this@BluetoothChatService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun connect(device: BluetoothDevice) {
        scope.launch {
            try {
                _connectionState.value = ConnectionState.Connecting
                socket = device.createRfcommSocketToServiceRecord(SPP_UUID)
                socket?.connect()

                inputStream = socket?.inputStream
                outputStream = socket?.outputStream

                // Безопасно получаем имя устройства
                val deviceName = getDeviceNameSafe(device)
                _connectionState.value = ConnectionState.Connected(deviceName ?: "Unknown")
                startListening()
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.Error(e.message ?: "Connection failed")
                closeConnection()
            }
        }
    }

    // Безопасное получение имени устройства
    private fun getDeviceNameSafe(device: BluetoothDevice): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                device.name
            } else {
                null
            }
        } else {
            device.name
        }
    }

    private fun startListening() {
        scope.launch {
            val buffer = ByteArray(4096)
            while (isActive) {
                try {
                    val bytes = inputStream?.read(buffer) ?: break
                    if (bytes > 0) {
                        val received = String(buffer, 0, bytes)
                        try {
                            val message = Json.decodeFromString<ChatMessage>(received)
                            _messages.value += message.copy(isFromMe = false)
                        } catch (e: Exception) {
                            val textMessage = ChatMessage(
                                text = received,
                                isFromMe = false
                            )
                            _messages.value += textMessage
                        }
                    }
                } catch (e: Exception) {
                    _connectionState.value = ConnectionState.Disconnected
                    break
                }
            }
        }
    }

    fun sendMessage(text: String) {
        scope.launch {
            try {
                val message = ChatMessage(text = text, isFromMe = true)
                val json = Json.encodeToString(message)
                outputStream?.write(json.toByteArray())
                outputStream?.flush()
                _messages.value += message
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendVoiceMessage(voiceBase64: String) {
        scope.launch {
            try {
                val message = ChatMessage(voiceBase64 = voiceBase64, isFromMe = true)
                val json = Json.encodeToString(message)
                outputStream?.write(json.toByteArray())
                outputStream?.flush()
                _messages.value += message
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun closeConnection() {
        scope.coroutineContext.cancelChildren()
        try {
            socket?.close()
            inputStream?.close()
            outputStream?.close()
        } catch (_: Exception) {}
        _connectionState.value = ConnectionState.Disconnected
    }

    override fun onDestroy() {
        closeConnection()
        scope.cancel()
        super.onDestroy()
    }
}