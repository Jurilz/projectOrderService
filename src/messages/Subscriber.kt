package com.orderService.messages

import com.orderService.commands.Command
import com.orderService.events.Event

interface Subscriber {

    suspend fun handle(event: Event)

    suspend fun handle(command: Command)
}