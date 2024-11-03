package com.cornmuffin.prototype.util.eventprocessor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class EventQueue<E : EventQueue.Item>(
    private val queue: ArrayDeque<E> = ArrayDeque()
) {
    private val _nextEvent = MutableSharedFlow<E>()
    val nextEvent = _nextEvent.asSharedFlow()

    suspend fun add(event: E) {
        addToQueue(event)
        queue.removeFirstOrNull()?.let { _nextEvent.emit(it) }
    }

    @Synchronized
    @Suppress("Unused")
    fun debug() {
        queue.forEachIndexed { index, event ->
            println("=> [$index] $event")
        }
    }

    interface Item {
        fun isTopPriority() = false
    }

    private fun addToQueue(event: E) {
        val existingIndex = queue.indexOfFirst { it::class == event::class }

        if (existingIndex >= 0) {
            // Replace existing event of this type with newer one
            queue[existingIndex] = event
        } else if (event.isTopPriority()) {
            // A top-priority event will be the next one to be popped
            queue.addFirst(event)
        } else {
            // Anything else goes to the back of the queue
            queue.addLast(event)
        }
    }
}
