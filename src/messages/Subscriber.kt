package com.orderService.messages

import com.orderService.commands.Command
import com.orderService.events.Event

interface Subscriber {

    suspend fun handleEvent(event: Event)

    suspend fun handleCommand(command: Command)
}