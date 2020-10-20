package com.orderService.projectors

import com.orderService.domain.Order
import com.orderService.domain.OrderState
import com.orderService.events.*
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.repository.OrderReadRepository

class DefaultOrderProjector(private val orderReadRepository: OrderReadRepository): OrderProjector {

    override suspend fun project(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun addOrder(orderCreatedEvent: OrderCreatedEvent) {
        val order = Order(
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

    override suspend fun updateOrder(orderUpdatedEvent: OrderUpdatedEvent) {
        val updateOrder = Order(
            orderId = orderUpdatedEvent.orderEvent.orderId,
            productName = orderUpdatedEvent.orderEvent.productName,
            amount = orderUpdatedEvent.orderEvent.amount,
            customerName = orderUpdatedEvent.orderEvent.customerName,
            address = orderUpdatedEvent.orderEvent.address,
            lastModified = orderUpdatedEvent.orderEvent.lastModified,
            state = orderUpdatedEvent.orderEvent.state
        )
        orderReadRepository.update(updateOrder)
    }

    override suspend fun deleteOrder(orderDeletedEvent: OrderDeletedEvent) {
        orderReadRepository.delete(orderDeletedEvent.orderEvent.orderId)
    }
}