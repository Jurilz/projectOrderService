package com.orderService.projectors

import com.orderService.domain.Order
import com.orderService.events.*
import com.orderService.events.orderEvents.*
import com.orderService.repository.OrderReadRepository

class DefaultOrderProjector(private val orderReadRepository: OrderReadRepository): OrderProjector {

    override suspend fun project(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun addOrder(orderCreatedEvent: OrderCreatedEvent) {
        val order: Order = orderCreatedEvent.orderEvent.buildOrder()
        orderReadRepository.insert(order)
    }

    override suspend fun updateOrder(orderUpdatedEvent: OrderUpdatedEvent) {
        val updateOrder: Order = orderUpdatedEvent.orderEvent.buildOrder()
        orderReadRepository.update(updateOrder)
    }

    override suspend fun deleteOrder(orderDeletedEvent: OrderDeletedEvent) {
        orderReadRepository.delete(orderDeletedEvent.orderEvent.orderId)
    }
}