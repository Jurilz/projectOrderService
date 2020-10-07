package com.orderService.projectors

import com.orderService.events.Event
import com.orderService.events.OrderCreatedEvent

interface OrderProjector {

    suspend fun project(event: Event)

    suspend fun addOrder(orderCreatedEvent: OrderCreatedEvent)


}