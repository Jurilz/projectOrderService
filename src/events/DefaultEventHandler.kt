package com.orderService.events


import com.orderService.commands.Command
import com.orderService.domain.OrderState
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.messages.DefaultEventBroker
import com.orderService.projectors.OrderProjector

class DefaultEventHandler(
    private val orderProjector: OrderProjector
): EventHandler {

    override suspend fun handle(event: Event) {
        when(event) {
            is OrderCreatedEvent -> orderProjector.addOrder(event)
            is OrderUpdatedEvent -> handleOrderUpdate(event)
            is OrderDeletedEvent -> orderProjector.deleteOrder(event)
        }
    }

    override suspend fun handle(command: Command) {
        TODO("Not yet implemented")
    }

    private suspend fun handleOrderUpdate(event: OrderUpdatedEvent) {
        when(event.orderEvent.state) {
            OrderState.cancelationProccedByWarehouse.toString() -> deleteOrder(event)
            else -> orderProjector.updateOrder(event)
        }
    }

    private suspend fun deleteOrder(event: OrderUpdatedEvent) {
        val deleteEvent = OrderEvent(
            orderId = event.orderEvent.orderId,
            productName = event.orderEvent.productName,
            customerName = event.orderEvent.customerName,
            amount = event.orderEvent.amount,
            address = event.orderEvent.address,
            state = event.orderEvent.state
        )
        deleteEvent.lastModified = event.lastModified
        val deleteOrderEvent = OrderDeletedEvent(deleteEvent)
        orderProjector.deleteOrder(deleteOrderEvent)
    }
}