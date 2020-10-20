package com.orderService.commands

import com.orderService.messages.Subscriber

interface CommandHandler: Subscriber {

    override suspend fun handle(command: Command)

}