package com.cornmuffin.fab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FabViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(FabState.PRIMARY)
    internal val stateFlow: StateFlow<FabState> = _stateFlow.asStateFlow()
    private val _nextEvent: MutableSharedFlow<FabEvent> = MutableSharedFlow()
    private val nextEvent = _nextEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            nextEvent.collect {
                control(it)
            }
        }
    }

    suspend fun reduce(event: FabEvent) {
        _nextEvent.emit(event)
    }

    private val control: (FabEvent) -> Unit = { event: FabEvent ->
        when (event) {
            is FabEvent.BecomePrimary -> becomePrimary()
            is FabEvent.BecomeSecondary -> becomeSecondary()
        }
    }

    private fun becomePrimary() {
        _stateFlow.value = FabState.PRIMARY
    }

    private fun becomeSecondary() {
        _stateFlow.value = FabState.SECONDARY
    }
}
