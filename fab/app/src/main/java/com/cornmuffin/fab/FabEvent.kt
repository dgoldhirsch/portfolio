package com.cornmuffin.fab

sealed interface FabEvent {
    data object BecomePrimary : FabEvent
    data object BecomeSecondary : FabEvent
}
