package com.orderService.projectors

import com.orderService.events.Event
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.messages.Subscriber

interface OrderProjector {

    suspend fun project(event: Event)

    suspend fun addOrder(orderCreatedEvent: OrderCreatedEvent)

    suspend fun updateOrder(orderUpdatedEvent: OrderUpdatedEvent)

    suspend fun deleteOrder(orderDeletedEvent: OrderDeletedEvent)

}