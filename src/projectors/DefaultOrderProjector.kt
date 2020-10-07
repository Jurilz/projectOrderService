package com.orderService.projectors

import com.orderService.domain.Order
import com.orderService.events.Event
import com.orderService.events.OrderCreatedEvent
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic
import com.orderService.repository.OrderReadRepository

class DefaultOrderProjector(private val orderReadRepository: OrderReadRepository): OrderProjector {

    override suspend fun project(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun addOrder(orderCreatedEvent: OrderCreatedEvent) {
        val order: Order = Order(
            orderCreatedEvent.orderEvent.orderId,
            orderCreatedEvent.orderEvent.productName,
            orderCreatedEvent.orderEvent.amount,
            orderCreatedEvent.orderEvent.customerName,
            orderCreatedEvent.orderEvent.address,
            orderCreatedEvent.orderEvent.lastModified,
            orderCreatedEvent.orderEvent.state
        )
        orderReadRepository.insert(order)
    }
}