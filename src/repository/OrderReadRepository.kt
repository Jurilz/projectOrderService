package com.orderService.repository

import com.orderService.domain.Order

interface OrderReadRepository {
    suspend fun getAll(): List<Order>
}