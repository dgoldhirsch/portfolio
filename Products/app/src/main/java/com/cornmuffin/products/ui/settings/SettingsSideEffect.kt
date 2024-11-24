package com.cornmuffin.products.ui.settings

import com.cornmuffin.products.data.settings.Settings

internal sealed interface SettingsSideEffect {
    data object Load : SettingsSideEffect
    data class WriteSettingsToDisk(val newSettings: Settings) : SettingsSideEffect
}
