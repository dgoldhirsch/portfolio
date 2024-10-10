package com.example.eathandmoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eathandmoon.ui.theme.EathAndMoonTheme
import kotlinx.coroutines.delay
import kotlin.math.abs

private const val EARTH_ORBIT_RADIUS = 200 // Laughably too small--should be more like 1280
private const val MOON_RADIUS = 5 // Smallest, reasonable value for the UX
private const val EARTH_RADIUS = MOON_RADIUS * 4 // Actually true
private const val MOON_ORBIT_RADIUS = 60 // Pretty close to true with respect to Earth radius
private const val SUN_RADIUS = 10 // Far too small.  The sun is actually 109 times the Earth radius
private const val EARTH_NAME = "Earth"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EathAndMoonTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        PlanetarySystem()
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PlanetarySystem() {
    var angleEarth by remember { mutableDoubleStateOf(0.0) }
    var angleMoon by remember { mutableDoubleStateOf(0.0) }
    var earthX by remember { mutableFloatStateOf(0.0f) }
    var earthY by remember { mutableFloatStateOf(0.0f) }

    var isRunning by remember { mutableStateOf(true) }
    var isHovering by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()
    val hoverTextStyle =
        TextStyle(
            color = Color.Black,
            fontSize = EARTH_RADIUS.sp,
        )
    val textLayoutResult =
        remember(EARTH_NAME) {
            textMeasurer.measure(EARTH_NAME, hoverTextStyle)
        }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            // We can implement this animation using a single frame rate.  If the animation
            // involved more objects and/or movements that were independent of each other,
            // we could instead use Android's "infinite transition" animation functions. (Under
            // the hood, Compose would have to figure out the underlying, basic frame rate.)
            //
            // There are two UI design choices:
            //  - To obtain a visually "smooth" experience, the frame rate is 10 milliseconds.
            //  - We animate the moon such that it orbits the earth in 1 second.
            //
            // In real life, the Moon orbits the Earth in 27.4 days.
            // With a frame rate of 10 milliseconds, the Moon advances about 3.6 degrees each frame.
            angleMoon -= 3.6

            // The Earth orbits the Sun degrees in 365.256 days, which is about 13.383 times slower
            // the speed with which the Moon orbits the Earth.  For each frame, therefore, the Earth
            // should advance 1/13.383 = 0.07472 degrees.
            angleEarth -= 0.07472

            // Frame rate, chosen for a "smooth" experience.
            delay(10L)
        }
    }

    Canvas(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable {
                    isRunning = !isRunning
                }.pointerInput(Unit) {
                    detectDragGestures { change: PointerInputChange, _: Offset ->
                        change.consume() // https://developer.android.com/develop/ui/compose/touch-input/pointer-input/drag-swipe-fling

                        isHovering =
                            abs(change.position.x - earthX) <= EARTH_ORBIT_RADIUS &&
                            abs(change.position.y - earthY) <= EARTH_ORBIT_RADIUS
                    }
                },
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // SUN
        drawCircle(
            color = Color(0xffffa588),
            radius = SUN_RADIUS.dp.toPx(),
            center = Offset(centerX, centerY),
        )

        // EARTH ORBIT
        drawCircle(
            color = Color.Gray,
            radius = EARTH_ORBIT_RADIUS.dp.toPx(),
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.dp.toPx()),
        )

        // Earth's position
        earthX =
            centerX + EARTH_ORBIT_RADIUS.dp.toPx() *
            kotlin.math
                .cos(Math.toRadians(angleEarth))
                .toFloat()
        earthY =
            centerY + EARTH_ORBIT_RADIUS.dp.toPx() *
            kotlin.math
                .sin(Math.toRadians(angleEarth))
                .toFloat()

        // EARTH
        drawCircle(
            color = Color.Blue,
            radius = EARTH_RADIUS.dp.toPx(),
            center = Offset(earthX, earthY),
        )

        // "Earth" hover-text
        if (isHovering) {
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(earthX, earthY - 125),
            )
        }

        // MOON ORBIT
        drawCircle(
            color = Color.Gray,
            radius = MOON_ORBIT_RADIUS.dp.toPx(),
            center = Offset(earthX, earthY),
            style = Stroke(width = 1.dp.toPx()),
        )

        // Moon's position
        val moonX =
            earthX + MOON_ORBIT_RADIUS.dp.toPx() *
                kotlin.math
                    .cos(Math.toRadians(angleMoon))
                    .toFloat()
        val moonY =
            earthY + MOON_ORBIT_RADIUS.dp.toPx() *
                kotlin.math
                    .sin(Math.toRadians(angleMoon))
                    .toFloat()

        // MOON
        drawCircle(
            color = Color.Gray,
            radius = MOON_RADIUS.dp.toPx(),
            center = Offset(moonX, moonY),
        )
    }
}
