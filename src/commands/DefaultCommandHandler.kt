package com.orderService.commands

import com.orderService.events.*
import com.orderService.messages.MessageBroker
import com.orderService.messages.MessageTopic

class DefaultCommandHandler(private val messageBroker: MessageBroker):
    CommandHandler {

    override suspend fun handleCommand(command: Command) {
        when(command) {
            is CreateOrderCommand -> messageBroker.publishCommand(MessageTopic.ORDER_SERVICE, command)
            is UpdateOrderCommand -> messageBroker.publishCommand(MessageTopic.ORDER_SERVICE, command)
            is DeleteOrderCommand -> messageBroker.publishCommand(MessageTopic.ORDER_SERVICE, command)
        }
    }

    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }

}

