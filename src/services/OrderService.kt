package com.orderService.services

import com.orderService.domain.Order
import com.orderService.events.Event
import com.orderService.messages.Subscriber

interface OrderService: Subscriber {

    suspend fun storeAndPublishOrderEvent(event: Event)

    override suspend fun handle(event: Event)

    suspend fun getAll(): List<Order>

    suspend fun getById(orderId: String): Order?

    suspend fun getByCustomerName(customerName: String): List<Order>?

    suspend fun getReadyToPick(): List<Order>?
}