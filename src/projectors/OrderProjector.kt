package com.orderService.projectors

import com.orderService.events.Event
import com.orderService.events.OrderCreatedEvent
import com.orderService.events.OrderDeletedEvent
import com.orderService.events.OrderUpdatedEvent

interface OrderProjector {

    suspend fun project(event: Event)

    suspend fun addOrder(orderCreatedEvent: OrderCreatedEvent)

    suspend fun updateOrder(orderUpdatedEvent: OrderUpdatedEvent)

    suspend fun deleteOrder(orderDeletedEvent: OrderDeletedEvent)

}