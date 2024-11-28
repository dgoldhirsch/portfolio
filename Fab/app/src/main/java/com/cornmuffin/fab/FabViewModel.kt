package com.cornmuffin.fab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FabViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(FabState.PRIMARY)

    /**
     * Observable state for [FabLayout] UI.
     */
    internal val stateFlow: StateFlow<FabState> = _stateFlow.asStateFlow()

    /**
     * View model queue of events to which to respond, such as clicks coming
     * from the [FabLayout] UI or from internal processing of the view model.
     */
    private val _nextEvent: MutableSharedFlow<FabEvent> = MutableSharedFlow()
    private val nextEvent = _nextEvent.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
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
            is FabEvent.BecomePrimary -> _stateFlow.value = FabState.PRIMARY
            is FabEvent.BecomeSecondary -> _stateFlow.value = FabState.SECONDARY
        }
    }
}
