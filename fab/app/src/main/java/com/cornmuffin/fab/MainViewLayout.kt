package com.cornmuffin.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// Copied this from Icons.Default.Remove, after bringing in the extended Material icon library,
// In app build.gradle.kts: implementation("androidx.compose.material:material-icons-extended")
// Then removed the dependency because (we are told) the extended library will bloat the APK.
// Learned about this from https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
private val minus: ImageVector
    get() {
        if (rawMinus != null) {
            return rawMinus!!
        }
        rawMinus = materialIcon(name = "Filled.Remove") {
            materialPath {
                moveTo(19.0f, 13.0f)
                horizontalLineTo(5.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineToRelative(14.0f)
                verticalLineToRelative(2.0f)
                close()
            }
        }
        return rawMinus!!
    }

private var rawMinus: ImageVector? = null

@Composable
fun FabLayout() {
    val viewModel = viewModel<MainViewModel>()
    val mainState by viewModel.stateFlow.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { ButtonsBasedOnState(mainState) },
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
fun ButtonsBasedOnState(state: MainState) {
    val primaryAlpha: Float by animateFloatAsState(
        targetValue = if (state == MainState.PRIMARY) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "PrimaryButton Alpha Animation",
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(100.dp),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = state == MainState.SECONDARY,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                    slideInVertically(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) +
                    slideOutVertically(animationSpec = tween(durationMillis = 1000)),
        ) {
            SecondaryButtonList(minus = minus)
        }

        PrimaryButton(alpha = primaryAlpha)
    }
}

@Composable
private fun PrimaryButton(alpha: Float) {
    val viewModel = viewModel<MainViewModel>()

    FloatingActionButton(
        onClick = { viewModel.enqueue(MainEvent.BecomeSecondary) },
        shape = CircleShape,
        containerColor = colorResource(R.color.primary_button),
        modifier = Modifier.graphicsLayer(alpha = alpha)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Primary button",
        )
    }
}

@Composable
private fun SecondaryButtonList(minus: ImageVector) {
    Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Atom")
            }
            Column {
                SecondaryButton(contentDescription = "Atom", imageVector = minus)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Blaster")
            }
            Column {
                SecondaryButton(contentDescription = "Blaster", imageVector = minus)
            }
        }
    }
}

@Composable
private fun SecondaryButton(contentDescription: String, imageVector: ImageVector) {
    val viewModel = viewModel<MainViewModel>()

    SmallFloatingActionButton(
        onClick = { viewModel.enqueue(MainEvent.BecomePrimary) },
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
