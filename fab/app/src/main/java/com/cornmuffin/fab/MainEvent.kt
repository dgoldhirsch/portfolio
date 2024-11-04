package com.cornmuffin.fab

sealed interface MainEvent {
    data object BecomePrimary : MainEvent
    data object BecomeSecondary : MainEvent
}
