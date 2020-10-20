package com.orderService.commands

import com.orderService.domain.OrderState
import com.orderService.events.*
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic

class DefaultCommandHandler(private val eventBroker: EventBroker):
    CommandHandler {

    override suspend fun handle(command: Command) {
        when(command) {
            is CreateOrderCommand -> addOrder(command)
            is UpdateOrderCommand -> updateOrder(command)
            is DeleteOrderCommand -> handleDeleteCommand(command)
        }
    }

    private suspend fun handleDeleteCommand(command: DeleteOrderCommand) {
        when(command.orderCommand.state){
            OrderState.pending.toString() -> deleteOrder(command)
            OrderState.processedByOrderService.toString() -> deleteOrder(command)
            OrderState.canceledByWarehouse.toString() -> deleteOrder(command)
            OrderState.processedByWarehouse.toString() -> cancelOrder(command)
            OrderState.readyToPick.toString() -> cancelOrder(command)
            OrderState.beingDelivered.toString() -> cancelOrder(command)
        }
    }

    private suspend fun cancelOrder(command: DeleteOrderCommand) {
        val cancelEvent: OrderEvent = buildCancelEvent(command.orderCommand)
        val orderCanceledEvent = OrderUpdatedEvent(cancelEvent)
        eventBroker.publish(EventTopic.ORDER_SERVICE, orderCanceledEvent)
    }

    private suspend fun deleteOrder(command: DeleteOrderCommand) {
        val deletedEvent: OrderEvent = buildCancelEvent(command.orderCommand)
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
        val orderEvent = OrderEvent(
            orderId = orderCommand.orderId,
            productName = orderCommand.productName,
            customerName = orderCommand.customerName,
            amount = orderCommand.amount,
            address = orderCommand.address,
            state = orderCommand.state
        )
        orderEvent.lastModified = orderCommand.lastModified
        return orderEvent
    }

    private fun buildCancelEvent(orderCommand: OrderCommand): OrderEvent {
        val newState: String = determineCancellationState(orderCommand)

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

    private fun determineCancellationState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.processedByWarehouse.toString() -> OrderState.cancelationProccedByOrderService.toString()
            OrderState.readyToPick.toString() -> OrderState.cancelationProccedByOrderService.toString()
            OrderState.beingDelivered.toString() -> OrderState.canceledInDelivery.toString()
            else -> orderCommand.state
        }
    }

    override suspend fun handle(event: Event) {
        TODO("Not yet implemented")
    }

}

