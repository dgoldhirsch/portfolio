package com.cornmuffin.products

import androidx.compose.runtime.compositionLocalOf
import com.cornmuffin.products.data.settings.Settings

val LocalSettings = compositionLocalOf<Settings> { error("No Settings for LocalSettings provider?!") }
