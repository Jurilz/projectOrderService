package com.orderService.events


import com.orderService.commands.Command
import com.orderService.domain.OrderState
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.projectors.OrderProjector

class DefaultEventHandler(
    private val orderProjector: OrderProjector
): EventHandler {

    override suspend fun handleEvent(event: Event) {
        when(event) {
            is OrderCreatedEvent -> orderProjector.addOrder(event)
            is OrderUpdatedEvent -> orderProjector.updateOrder(event)
            is OrderDeletedEvent -> orderProjector.deleteOrder(event)
        }
    }

    //TODO: remove
    private suspend fun handleOrderUpdate(event: OrderUpdatedEvent) {
        when(event.orderEvent.state) {
            OrderState.cancelationProccedByWarehouse.toString() -> deleteOrder(event)
            else -> orderProjector.updateOrder(event)
        }
    }

    //TODO: remove
    private suspend fun deleteOrder(event: OrderUpdatedEvent) {
        val deleteOrderEvent = OrderDeletedEvent(event.orderEvent)
        orderProjector.deleteOrder(deleteOrderEvent)
    }

    override suspend fun handleCommand(command: Command) {
        TODO("Not yet implemented")
    }
}