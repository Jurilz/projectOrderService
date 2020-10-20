package com.orderService.rabbitMQ

import com.orderService.commands.Command
import com.orderService.events.Event
import com.orderService.messages.Subscriber

interface RabbitProvider: Subscriber {

    fun declareAndBindEventQueue()

    override suspend fun handleEvent(event: Event)

    override suspend fun handleCommand(command: Command)
}