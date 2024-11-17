package com.cornmuffin.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel

// Copied this from Icons.Default.Remove, after bringing in the extended Material icon library,
// In app build.gradle.kts: implementation("androidx.compose.material:material-icons-extended")
// Then removed the dependency because (we are told) the extended library will bloat the APK.
// Learned about this from https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
private val minus: ImageVector
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

@Composable
fun FabLayout() {
    val viewModel = viewModel<MainViewModel>()
    val mainState = viewModel.stateFlow.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            when (mainState.value) {
                MainState.PRIMARY -> PrimaryButton(onClick = { viewModel.enqueue(MainEvent.BecomeSecondary) })
                MainState.SECONDARY -> SecondaryButtonList(
                    minus,
                    onClick = { viewModel.enqueue(MainEvent.BecomePrimary) }
                )
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
                .background(color = colorResource(R.color.content_background))
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text("Content goes here")
        }
    }
}

@Composable
private fun PrimaryButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = colorResource(R.color.primary_button),
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Primary button",
            modifier = Modifier.background(color = colorResource(R.color.primary_button)),
        )
    }
}

@Composable
private fun SecondaryButtonList(minus: ImageVector, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Atom")
            }
            Column {
                SecondaryButton(contentDescription = "Atom", imageVector = minus, onClick = onClick)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Blaster")
            }
            Column {
                SecondaryButton(contentDescription = "Blaster", imageVector = minus, onClick = onClick)
            }
        }
    }
}

@Composable
private fun SecondaryButton(contentDescription: String, imageVector: ImageVector, onClick: () -> Unit = {}) {
    SmallFloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = colorResource(R.color.secondary_button),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.background(color = colorResource(R.color.secondary_button))
        )
    }
}
