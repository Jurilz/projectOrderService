package com.orderService.events

import com.orderService.messages.Subscriber

interface EventHandler: Subscriber {

    override suspend fun handleEvent(event: Event)
}