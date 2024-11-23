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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// There's no default "minus" image vector in the standard Material library.
// We got this code by adding implementation("androidx.compose.material:material-icons-extended")
// to app build.gradle.kts, copying the code, and then removing the dependency.
// Learned about it from https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
private var rawMinus: ImageVector = materialIcon(name = "Filled.Remove") {
    materialPath {
        moveTo(19.0f, 13.0f)
        horizontalLineTo(5.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(14.0f)
        verticalLineToRelative(2.0f)
        close()
    }
}

val LocalMinus = staticCompositionLocalOf { rawMinus }

@Composable
fun FabLayout() {
    val mainState by viewModel<FabViewModel>().stateFlow.collectAsState()

    CompositionLocalProvider(LocalMinus provides rawMinus) {
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
}

@Composable
fun ButtonsBasedOnState(state: FabState) {
    val primaryAlpha: Float by animateFloatAsState(
        targetValue = if (state == FabState.PRIMARY) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "PrimaryButton alpha animation",
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End, // crucial, else primary button slides to the left when clicked
    ) {
        AnimatedVisibility(
            visible = state == FabState.SECONDARY,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                    slideInVertically(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) +
                    slideOutVertically(animationSpec = tween(durationMillis = 1000)),
            label = "Secondary button list visibility animation",
        ) {
            SecondaryButtonList()
        }

        // Can't use visibility animation, because we want secondaries to appear above its place in the column.
        // Instead, animate content.
        PrimaryButton(alpha = primaryAlpha)
    }
}

@Composable
private fun PrimaryButton(alpha: Float) {
    val viewModel = viewModel<FabViewModel>()

    FloatingActionButton(
        onClick = { viewModel.enqueue(FabEvent.BecomeSecondary) },
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
private fun SecondaryButton(contentDescription: String, imageVector: ImageVector) {
    val viewModel = viewModel<FabViewModel>()

    SmallFloatingActionButton(
        onClick = { viewModel.enqueue(FabEvent.BecomePrimary) },
        shape = CircleShape,
        containerColor = colorResource(R.color.secondary_button),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun SecondaryButtonList() {
    Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Atom")
            }
            Column {
                SecondaryButton(contentDescription = "Atom", imageVector = LocalMinus.current)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Blaster")
            }
            Column {
                SecondaryButton(contentDescription = "Blaster", imageVector = LocalMinus.current)
            }
        }
    }
}
