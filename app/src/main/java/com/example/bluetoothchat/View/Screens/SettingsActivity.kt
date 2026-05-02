package com.example.bluetoothchat.View.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.remember

private data object RouteS

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            Text("HELLO SETTINGS")
            val backStack = remember { mutableListOf<Any>(RouteS) }
            

        }
    }
}
//class BasicActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        setEdgeToEdgeConfig()
//        super.onCreate(savedInstanceState)
//        setContent {