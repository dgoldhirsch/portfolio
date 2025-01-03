package com.cornmuffin.products.util

import java.time.ZonedDateTime
import javax.inject.Inject

class TimeControl @Inject constructor() {
    fun now() = ZonedDateTime.now()
}
