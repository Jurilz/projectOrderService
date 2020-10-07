package com.orderService.messages

import com.orderService.events.Event

interface Subscriber {

    suspend fun handle(event: Event)
}