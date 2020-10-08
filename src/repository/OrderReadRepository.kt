package com.orderService.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.orderService.domain.Order

interface OrderReadRepository {
    suspend fun getAll(): List<Order>

    suspend fun insert(order: Order)

    suspend fun getById(orderId: String): Order?

    suspend fun update(order: Order): UpdateResult

    suspend fun delete(orderId: String): DeleteResult

    suspend fun getByCustomerName(customerName: String): List<Order>?
}