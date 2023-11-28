package com.example.android.gainclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.android.gainclicker.ui.GAInClickerApp
import com.example.android.gainclicker.ui.theme.GAInClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GAInClickerTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    GAInClickerApp(modifier = Modifier.fillMaxSize())
                }

            }
        }
    }
}
