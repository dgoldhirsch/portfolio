package com.cornmuffin.fab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(MainState.PRIMARY)
    internal val stateFlow: StateFlow<MainState> = _stateFlow.asStateFlow()
    private val eventQueue: ArrayDeque<MainEvent> = ArrayDeque()
    private val _nextEvent: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    private val nextEvent = _nextEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            nextEvent.collect {
                control(it)
            }
        }
    }


    fun enqueue(event: MainEvent) {
        val existingIndex = eventQueue.indexOfFirst { it::class == event::class }

        if (existingIndex >= 0) {
            // Replace queued event with new one of this type
            eventQueue[existingIndex] = event
        } else {
            eventQueue.addLast(event)
        }
    }

    private val control: (MainEvent) -> Unit = { event: MainEvent ->
        when (event) {
            is MainEvent.BecomePrimary -> becomePrimary()
            is MainEvent.BecomeSecondary -> becomeSecondary()
        }
    }

    private fun becomePrimary() {
        _stateFlow.value = MainState.PRIMARY
    }

    private fun becomeSecondary() {
        _stateFlow.value = MainState.SECONDARY
    }
}
