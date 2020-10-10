package com.orderService.events


import com.orderService.projectors.OrderProjector

class DefaultEventHandler(private val orderProjector: OrderProjector): EventHandler {

    override suspend fun handle(event: Event) {
        when(event) {
            is OrderCreatedEvent -> orderProjector.addOrder(event)
            is OrderUpdatedEvent -> orderProjector.updateOrder(event)
            is OrderDeletedEvent -> orderProjector.deleteOrder(event)
        }
    }
}