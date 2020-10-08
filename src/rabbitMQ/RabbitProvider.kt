package com.orderService.rabbitMQ

import com.orderService.events.Event
import com.orderService.messages.Subscriber

interface RabbitProvider: Subscriber {

    fun declareAndBindEventQueue()

    override suspend fun handle(event: Event)
}