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
        val newState: String = determineNewState(orderCreatedEvent.orderEvent)
        val order: Order = Order(
            orderCreatedEvent.orderEvent.orderId,
            orderCreatedEvent.orderEvent.productName,
            orderCreatedEvent.orderEvent.amount,
            orderCreatedEvent.orderEvent.customerName,
            orderCreatedEvent.orderEvent.address,
            orderCreatedEvent.orderEvent.lastModified,
            newState
        )
        orderReadRepository.insert(order)
    }

    override suspend fun updateOrder(orderUpdatedEvent: OrderUpdatedEvent) {
        val newState: String = determineNewState(orderUpdatedEvent.orderEvent)
        val updateOrder = Order(
            orderId = orderUpdatedEvent.orderEvent.orderId,
            productName = orderUpdatedEvent.orderEvent.productName,
            amount = orderUpdatedEvent.orderEvent.amount,
            customerName = orderUpdatedEvent.orderEvent.customerName,
            address = orderUpdatedEvent.orderEvent.address,
            lastModified = orderUpdatedEvent.orderEvent.lastModified,
            state = newState
        )
        orderReadRepository.update(updateOrder)
    }

    override suspend fun deleteOrder(orderDeletedEvent: OrderDeletedEvent) {
        orderReadRepository.delete(orderDeletedEvent.orderEvent.orderId)
    }

    private fun determineNewState(event: OrderEvent): String {
        return when(event.state) {
            OrderState.pending.toString() -> OrderState.processedByOrderService.toString()
            OrderState.processedByWarehouse.toString() -> OrderState.readyToPick.toString()
            else -> event.state
        }
    }
}