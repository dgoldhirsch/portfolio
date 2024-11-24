package com.cornmuffin.products.util

import kotlin.math.roundToInt

fun Double.roundToNearestHalf() = (this * 2).roundToInt() / 2.0
