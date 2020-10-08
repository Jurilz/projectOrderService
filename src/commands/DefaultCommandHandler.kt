package com.orderService.commands

import com.orderService.domain.OrderState
import com.orderService.events.*
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic

class DefaultCommandHandler(private val eventBroker: EventBroker):
    CommandHandler {

//    init {
//        eventBroker.subscribe(EventTopic.ORDER_COMMAND, this)
//    }

    override suspend fun handle(command: Command) {
        when(command) {
            is CreateOrderCommand -> addOrder(command)
            is UpdateOrderCommand -> updateOrder(command)
            is DeleteOrderCommand -> deleteOrder(command)
        }
    }

    private suspend fun deleteOrder(command: DeleteOrderCommand) {
        val deletedEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val orderDeletedEvent = OrderDeletedEvent(deletedEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderDeletedEvent)
    }

    private suspend fun updateOrder(command: UpdateOrderCommand) {
        val updateEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val orderUpdatedEvent  = OrderUpdatedEvent(updateEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderUpdatedEvent)
    }

    private suspend fun addOrder(command: CreateOrderCommand) {
        val orderEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val orderCreatedEvent = OrderCreatedEvent(orderEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderCreatedEvent)
    }


    private fun buildOrderEvent(orderCommand: OrderCommand): OrderEvent {
        val newState: String = determineNewState(orderCommand)

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

    private fun determineNewState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.pending.toString() -> OrderState.processedByOrderService.toString()
            else -> orderCommand.state
        }
    }

    override suspend fun handle(event: Event) {
        TODO("Not yet implemented")
    }

}

