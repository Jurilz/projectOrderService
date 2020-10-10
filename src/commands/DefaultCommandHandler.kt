package com.orderService.commands

import com.orderService.domain.OrderState
import com.orderService.events.*
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic

class DefaultCommandHandler(private val eventBroker: EventBroker):
    CommandHandler {

    override suspend fun handle(command: Command) {
        when(command) {
            is CreateOrderCommand -> addOrder(command)
            is UpdateOrderCommand -> updateOrder(command)
            is DeleteOrderCommand -> deleteOrder(command)
        }
    }

    override suspend fun deleteOrder(command: DeleteOrderCommand) {
        val deletedEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val orderDeletedEvent = OrderDeletedEvent(deletedEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderDeletedEvent)
    }

    override suspend fun updateOrder(command: UpdateOrderCommand) {
        val updateEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val orderUpdatedEvent  = OrderUpdatedEvent(updateEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderUpdatedEvent)
    }

    override suspend fun addOrder(command: CreateOrderCommand) {
        val orderEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val orderCreatedEvent = OrderCreatedEvent(orderEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderCreatedEvent)
    }


    override fun buildOrderEvent(orderCommand: OrderCommand): OrderEvent {
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

    override fun determineNewState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.pending.toString() -> OrderState.processedByOrderService.toString()
            else -> orderCommand.state
        }
    }

    override suspend fun handle(event: Event) {
        TODO("Not yet implemented")
    }

}

