package com.orderService.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.orderService.domain.Order
import org.jetbrains.exposed.sql.statements.InsertStatement

interface OrderReadRepository {
    suspend fun getAll(): List<Order>

    suspend fun insert(order: Order): InsertStatement<Number>

    suspend fun getById(orderId: String): Order?

    suspend fun update(order: Order): Int

    suspend fun delete(orderId: String): Int

    suspend fun getByCustomerName(customerName: String): List<Order>?

    suspend fun getReadyToPick(): List<Order>?

}