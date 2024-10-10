package com.cornmuffin.upperandlower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cornmuffin.upperandlower.ui.theme.UpperAndLowerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UpperAndLowerTheme {
                MainLayout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val LocalSheetState = compositionLocalOf<SheetState?> { null }
private val ALL_COLOR = Color.Red
private val TOP_COLOR = Color(0xaa, 0xff, 99)
private val BOTTOM_COLOR = Color(0xaa, 99, 0xff)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout() {
    val viewModel = viewModel<MainViewModel>()
    val mainState = viewModel.state.collectAsState().value
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(mainState) {
        when (mainState) {
            MainState.Uncovered -> sheetState.hide()
            else -> sheetState.show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopHalf()
        BottomHalf()
    }

    CompositionLocalProvider(LocalSheetState provides sheetState) {
        when (mainState) {
            MainState.ModalFull -> ModalFull { viewModel.uncover() }
            MainState.Modal25 -> ModalPartial(0.25f, "Expand to 50%") { viewModel.modal50() }
            MainState.Modal50 -> ModalPartial(0.5f, "Shrink to 25%") { viewModel.modal25() }
            else -> Unit
        }
    }
}

@Composable
private fun TopHalf() {
    val viewModel = viewModel<MainViewModel>()
    Half(0.5f, TOP_COLOR, "Cover All") { viewModel.modalFull() }
}

@Composable
private fun BottomHalf() {
    val viewModel = viewModel<MainViewModel>()

    // Tricky, entire height of bottom half of the column
    Half(1f, BOTTOM_COLOR, "Cover Bottom 50%") { viewModel.modal50() }
}

@Composable
private fun Half(
    coverage: Float,
    backgroundColor: Color,
    buttonText: String,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(coverage)
                .background(color = backgroundColor),
    ) {
        Button(onClick = onClick) {
            Text(text = buttonText)
        }
    }
}

@Composable
private fun ModalFull(onClick: () -> Unit) {
    BottomSheet(fillFromBottom = 1f) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = ALL_COLOR),
        ) {
            Button(onClick = onClick) {
                Text("Uncover All")
            }
        }
    }
}

@Composable
private fun ModalPartial(
    fillFromBottom: Float,
    buttonText: String,
    onClick: () -> Unit,
) {
    BottomSheet(fillFromBottom = fillFromBottom) {
        Button(onClick = onClick) {
            Text(buttonText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    fillFromBottom: Float,
    content: @Composable BoxScope.() -> Unit,
) {
    LocalSheetState.current?.let { sheetState ->
        val viewModel = viewModel<MainViewModel>()

        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(fillFromBottom),
            onDismissRequest = { viewModel.uncover() },
            sheetState = sheetState,
            content = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                    content = content,
                )
            },
        )
    }
}
