package com.orderService.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.orderService.database.Orders
import com.orderService.domain.Order
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresOrderReadRepository(database: Database): OrderReadRepository {

    init {
        transaction {
            SchemaUtils.create(Orders)
        }
    }

    override suspend fun getAll(): List<Order> {
        TODO("Not yet implemented")
//        return Orders.selectAll().toList()
    }

    override suspend fun insert(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun getById(orderId: String): Order? {
        TODO("Not yet implemented")
    }

    override suspend fun update(order: Order): UpdateResult {
        TODO("Not yet implemented")
    }

    override suspend fun delete(orderId: String): DeleteResult {
        TODO("Not yet implemented")
    }

    override suspend fun getByCustomerName(customerName: String): List<Order>? {
        TODO("Not yet implemented")
    }

}