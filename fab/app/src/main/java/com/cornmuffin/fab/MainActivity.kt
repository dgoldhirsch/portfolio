package com.cornmuffin.fab

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cornmuffin.fab.ui.theme.FabTheme

class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    // Copied this from Icons.Default.Remove, after bringing in the extended Material icon library,
    // In app build.gradle.kts: implementation("androidx.compose.material:material-icons-extended")
    // Then removed the dependency because (we are told) the extended library will bloat the APK.
    // Learned about this from https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
    val minus: ImageVector
        get() {
            if (_minus != null) {
                return _minus!!
            }
            _minus = materialIcon(name = "Filled.Remove") {
                materialPath {
                    moveTo(19.0f, 13.0f)
                    horizontalLineTo(5.0f)
                    verticalLineToRelative(-2.0f)
                    horizontalLineToRelative(14.0f)
                    verticalLineToRelative(2.0f)
                    close()
                }
            }
            return _minus!!
        }

    private var _minus: ImageVector? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FabTheme {
                val mainState = viewModel.stateFlow.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        when (mainState.value) {
                            MainState.PRIMARY -> Secondary(minus)
                            MainState.SECONDARY -> Secondary(minus)
                        }
                    },

                    // This is the only way, so far, I've been able to keep the FABs
                    // at the bottom, right-hand corner while adding background
                    // content in the middle of the screen.
                    floatingActionButtonPosition = FabPosition.End,
                ) { paddingValues ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(color = Color(alpha = 0.5f, red = 1f, green = 0.9f, blue = 0.6f))
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        Text("Content goes here")
                    }
                }
            }
        }
    }
}

@Composable
private fun Primary() {
    FloatingActionButton(
        onClick = { },
        shape = CircleShape,
        modifier = Modifier
    ) {
        Icon(Icons.Filled.Add, "Floating action button")
    }
}

@Composable
private fun Secondary(minus: ImageVector) {
    Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Atom")
            }
            Column {
                SmallFloatingActionButton(
                    onClick = { },
                    shape = CircleShape,
                    modifier = Modifier
                ) {
                    Icon(minus, "Atom")
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Blaster")
            }
            Column {
                SmallFloatingActionButton(
                    onClick = { },
                    shape = CircleShape,
                    modifier = Modifier
                ) {
                    Icon(minus, "Blaster")
                }
            }
        }
    }
}
