package com.orderService.commands

import com.orderService.messages.Subscriber

interface CommandHandler: Subscriber {

    suspend fun handle(command: Command)

}