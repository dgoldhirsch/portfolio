package com.cornmuffin.fab

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cornmuffin.fab.ui.theme.FabTheme

class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FabTheme {
                FabLayout()
            }
        }
    }
}
