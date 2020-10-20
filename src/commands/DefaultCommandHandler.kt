package com.orderService.commands

import com.orderService.events.*
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic

class DefaultCommandHandler(private val eventBroker: EventBroker):
    CommandHandler {

    override suspend fun handleCommand(command: Command) {
        when(command) {
            is CreateOrderCommand -> eventBroker.publishCommand(EventTopic.ORDER_SERVICE, command)
            is UpdateOrderCommand -> eventBroker.publishCommand(EventTopic.ORDER_SERVICE, command)
            is DeleteOrderCommand -> eventBroker.publishCommand(EventTopic.ORDER_SERVICE, command)
        }
    }

    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }

}

