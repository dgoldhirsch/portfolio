package com.cornmuffin.tabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.cornmuffin.tabs.ui.theme.TabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TabsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScrollableTabExercise(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

private data class State(
    val title: String,
    val iconComposable: @Composable () -> Unit,
    val color: Color = Color.Black,
) {
    constructor(
        title: String,
        imageVector: ImageVector,
        color: Color,
    ) : this(
        title = title,
        iconComposable = { Icon(imageVector = imageVector, contentDescription = null) },
        color = color,
    )

    constructor(
        title: String,
        @DrawableRes resId: Int,
        color: Color,
    ) : this(
        title = title,
        iconComposable = {
            Image(painter = painterResource(resId), contentDescription = "Image_$resId")
        },
        color = color,
    )
}

private val states =
    listOf(
        State(title = "Home", Icons.Default.Home, color = Color.Red),
        State(title = "About", R.drawable.peace, color = Color.Black),
        State(title = "Settings", Icons.Default.Settings, color = Color.Blue),
        State(title = "More", Icons.Default.Lock, color = Color(0.8f, 0.5f, 0.3f)),
        State(title = "Something", Icons.Default.ShoppingCart, Color.Green),
        State(title = "Everything", Icons.Default.Home, color = Color.Magenta),
    )

@Suppress("ktlint:standard:function-naming")
@Composable
fun ScrollableTabExercise(modifier: Modifier = Modifier) {
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        ScrollableTabRow(selectedTabIndex = tabIndex) {
            states.forEachIndexed { i, state ->
                Tab(
                    text = { Text(state.title) },
                    selected = tabIndex == i,
                    onClick = { tabIndex = i },
                    selectedContentColor = state.color,
                    unselectedContentColor = LocalContentColor.current,
                    icon = state.iconComposable,
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier =
                modifier
                    .fillMaxSize()
                    .background(color = states[tabIndex].color),
        ) {
            Text(
                text = states[tabIndex].title,
                color = textColorAgainstBackground(states[tabIndex].color),
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun textColorAgainstBackground(backgroundColor: Color) =
    if (backgroundColor.luminance() < 0.5) {
        Color.White
    } else {
        Color.Black
    }
