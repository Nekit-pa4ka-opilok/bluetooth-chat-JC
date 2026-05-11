package com.example.bluetoothchat.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothchat.model.ChatMessage
import com.example.bluetoothchat.service.BluetoothChatService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothService: BluetoothChatService? = null
    private var discoveryReceiver: BroadcastReceiver? = null
    private var context: Context? = null
    private var isServiceBound = false

    // ServiceConnection для привязки к сервису
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BluetoothChatService.LocalBinder
            bluetoothService = binder.getService()
            isServiceBound = true
            observeService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bluetoothService = null
            isServiceBound = false
        }
    }

    // Состояние подключения
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    // Список найденных устройств
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices.asStateFlow()

    // Статус сканирования
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    // История сообщений
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun initBluetooth(context: Context) {
        this.context = context
        startBluetoothService()
    }

    private fun startBluetoothService() {
        val context = this.context ?: return
        val intent = Intent(context, BluetoothChatService::class.java)
        context.startService(intent)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        if (!checkBluetoothPermission()) return

        _isScanning.value = true
        _devices.value = emptyList()

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }

        discoveryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            if (ActivityCompat.checkSelfPermission(
                                    context!!,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                        }
                        @SuppressLint("MissingPermission")
                        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        device?.let { dev ->
                            val deviceName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                if (ActivityCompat.checkSelfPermission(
                                        context!!,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) dev.name else null
                            } else {
                                dev.name
                            }
                            if (!deviceName.isNullOrBlank() && !_devices.value.any { it.address == dev.address }) {
                                _devices.value += dev
                            }
                        }
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        _isScanning.value = false
                        context?.unregisterReceiver(this)
                    }
                }
            }
        }

        context?.registerReceiver(discoveryReceiver, filter)
        bluetoothAdapter?.startDiscovery()
    }

    fun stopDiscovery() {
        if (checkBluetoothPermission()) {
            bluetoothAdapter?.cancelDiscovery()
            _isScanning.value = false
            discoveryReceiver?.let {
                try {
                    context?.unregisterReceiver(it)
                } catch (_: Exception) { }
            }
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        bluetoothService?.connect(device)
    }

    fun disconnect() {
        bluetoothService?.closeConnection()
        _connectionState.value = ConnectionState.Disconnected
    }

    fun sendMessage(text: String) {
        bluetoothService?.sendMessage(text)
    }

    fun sendVoiceMessage(voiceBase64: String) {
        bluetoothService?.sendVoiceMessage(voiceBase64)
    }

    fun observeService() {
        viewModelScope.launch {
            bluetoothService?.messages?.collect { newMessages ->
                _messages.value = newMessages
            }
        }
        viewModelScope.launch {
            bluetoothService?.connectionState?.collect { state ->
                _connectionState.value = state
            }
        }
    }

    private fun checkBluetoothPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = getApplication<Application>()
            return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        stopDiscovery()
        bluetoothService?.closeConnection()
        if (isServiceBound) {
            try {
                context?.unbindService(serviceConnection)
            } catch (_: Exception) { }
            isServiceBound = false
        }
    }
}

sealed class ConnectionState {
    data object Disconnected : ConnectionState()
    data object Connecting : ConnectionState()
    data class Connected(val deviceName: String) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}