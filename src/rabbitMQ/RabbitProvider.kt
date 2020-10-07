package com.orderService.rabbitMQ

import com.orderService.events.Event

interface RabbitProvider {

    fun declareAndBindEventQueue()

    suspend fun handle(event: Event)
}