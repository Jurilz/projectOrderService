package com.orderService.commands

import com.orderService.events.OrderCreatedEvent
import com.orderService.events.Event
import com.orderService.events.OrderEvent
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic
import com.orderService.messages.Subscriber

class DefaultCommandHandler(private val eventBroker: EventBroker):
    CommandHandler, Subscriber {

    init {
        eventBroker.subscribe(EventTopic.ORDER_COMMAND, this)
    }

    override suspend fun handle(command: Command) {
        when(command) {
            is CreateOrderCommand -> addOrder(command)
        }
    }

    private suspend fun addOrder(command: CreateOrderCommand) {
        val orderEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val createOrderEvent = OrderCreatedEvent(orderEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, createOrderEvent)
    }


    private fun buildOrderEvent(orderCommand: OrderCommand): OrderEvent {
        val newState: String = determineNewState()

        val orderEvent = OrderEvent(
            orderId = orderCommand.orderId,
            productName = orderCommand.productName,
            customerName = orderCommand.customerName,
            amount = orderCommand.amount,
            address = orderCommand.address,
            state = newState
        )
        orderEvent.lastModified = orderCommand.lastModified
        return orderEvent
    }

    private fun determineNewState(): String {
        TODO("Not yet implemented")
    }

    override suspend fun handle(event: Event) {
        TODO("Not yet implemented")
    }

}

