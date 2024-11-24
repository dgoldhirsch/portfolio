package com.cornmuffin.products.ui.settings

import com.cornmuffin.products.data.settings.Settings

data class SettingsViewModelState(
    val state: State = State.UNINITIALIZED,
    val settings: Settings = Settings(),
    val newSettings: Settings? = null,
) {
    fun asLoading() = SettingsViewModelState(state = State.LOADING)

    fun asSuccess(settings: Settings) = SettingsViewModelState(
        state = State.SUCCESSFUL,
        settings = settings,
    )

    enum class State {
        LOADING,
        SUCCESSFUL,
        UNINITIALIZED,
    }
}
