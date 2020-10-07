package com.orderService.repository

import com.orderService.domain.Order

interface OrderReadRepository {
    suspend fun getAll(): List<Order>

    suspend fun insert(order: Order)

    suspend fun getById(orderId: String): Order?
}