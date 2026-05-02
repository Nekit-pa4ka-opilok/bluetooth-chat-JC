package com.example.bluetoothchat.View.Elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun ChatHeader(){
    val items = listOf("Profile", "Settings", "Downloads")
//    val selectedItem = mutableStateOf(items)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState
        , drawerContent = {Text("content")}
        , scrimColor = Color(0xFF405033)
        , content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(77.dp)
                    .background(color = Color(0xFF475839))
                , horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                IconButton(
                    modifier = Modifier.padding(start = 10.dp, top = 34.dp)
                    , onClick = {scope.launch { drawerState.open() }}
                    , content = {
                        Icon(
                            Icons.Filled.Menu
                            , "Меню"
                            , modifier = Modifier.size(35.dp)
                            , tint = Color.LightGray
                        )
                    }
                )
                Text("GreenTooth"
                    , color = Color.White
                    , fontSize = 26.sp
                    , modifier = Modifier.padding(top = 38.dp)
                )
                Icon(Icons.Filled.AddCircle
                    , "Add"
                    , modifier = Modifier.padding(top = 34.dp, end = 10.dp)
                        .size(35.dp)
                    , tint = Color.LightGray
                )

            }
        }
    )
}