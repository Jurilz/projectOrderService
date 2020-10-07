package com.orderService.services

import com.orderService.domain.Order
import com.orderService.events.Event

interface OrderService {

    suspend fun storeAndPublishOrderEvent(event: Event)

    suspend fun handle(event: Event)

    suspend fun getAll(): List<Order>

    suspend fun getById(orderId: String): Order?
}