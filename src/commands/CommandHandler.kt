package com.orderService.commands

interface CommandHandler {

    suspend fun handle(command: Command)

}