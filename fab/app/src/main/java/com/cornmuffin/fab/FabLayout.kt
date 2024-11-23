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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val mainState by viewModel<FabViewModel>().stateFlow.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalMinus provides rawMinus,
    ) {
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
                Text(text = "Content goes here", color = colorResource(R.color.text))
            }
        }
    }
}

@Composable
fun ButtonsBasedOnState(state: FabState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End, // crucial, else primary button slides to the left when clicked
    ) {
        AnimatedVisibility(
            visible = state == FabState.SECONDARY,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                    slideInVertically(
                        animationSpec = tween(durationMillis = 1000),
                        initialOffsetY = { it / 2 }
                    ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) +
                    slideOutVertically(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetY = { it / 2 }
                    ),
            label = "Secondary button list visibility animation",
        ) {
            SecondaryButtonList()
        }

        PrimaryButton()
    }
}

@Composable
private fun PrimaryButton() {
    val viewModel = viewModel<FabViewModel>()
    val state = viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val alpha: Float by animateFloatAsState(
        targetValue = if (state.value == FabState.PRIMARY) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "PrimaryButton alpha animation",
    )

    FloatingActionButton(
        onClick = { scope.launch(Dispatchers.Default) { viewModel.reduce(FabEvent.BecomeSecondary) } },
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
    val scope = rememberCoroutineScope()

    SmallFloatingActionButton(
        onClick = { scope.launch(Dispatchers.Default) { viewModel.reduce(FabEvent.BecomePrimary) } },
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
                Text(text = "Atom", color = colorResource(R.color.text))
            }
            Column {
                SecondaryButton(contentDescription = "Atom", imageVector = LocalMinus.current)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Blaster", color = colorResource(R.color.text))
            }
            Column {
                SecondaryButton(contentDescription = "Blaster", imageVector = LocalMinus.current)
            }
        }
    }
}
