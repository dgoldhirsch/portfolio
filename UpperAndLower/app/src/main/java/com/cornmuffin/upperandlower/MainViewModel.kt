package com.cornmuffin.upperandlower

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainState.Uncovered)
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun modalFull() = _state.update { MainState.ModalFull }
    fun modal50() = _state.update { MainState.Modal50 }
    fun modal25() = _state.update { MainState.Modal25 }
    fun uncover() = _state.update { MainState.Uncovered }
}
