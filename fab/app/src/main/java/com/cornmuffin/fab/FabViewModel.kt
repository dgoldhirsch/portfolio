package com.cornmuffin.fab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class FabViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(FabState.PRIMARY)
    internal val stateFlow: StateFlow<FabState> = _stateFlow.asStateFlow()
    private val eventQueue: ArrayDeque<FabEvent> = ArrayDeque()
    private val _nextEvent: MutableSharedFlow<FabEvent> = MutableSharedFlow()
    private val nextEvent = _nextEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            advance() // emit initial value of shared flow

            nextEvent
                .filterNotNull() // probably unnecessary, but protects [control]
                .collect {
                control(it)
            }
        }
    }

    private fun advance() {
        eventQueue.removeFirstOrNull()?.also {
            viewModelScope.launch {
                _nextEvent.emit(it)
            }
        }
    }

    fun enqueue(event: FabEvent) {
        val existingIndex = eventQueue.indexOfFirst { it::class == event::class }

        if (existingIndex >= 0) {
            // Replace queued event with new one of this type
            eventQueue[existingIndex] = event
        } else {
            eventQueue.addLast(event)
        }

        advance()
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
