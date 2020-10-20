package com.orderService.repository

import com.orderService.database.Orders
import com.orderService.domain.Order
import com.orderService.domain.OrderState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresOrderReadRepository(database: Database): OrderReadRepository {

    init {
        transaction {
            SchemaUtils.create(Orders)
        }
    }

    override suspend fun getAll(): List<Order> {
        return withContext(Dispatchers.IO) {
            transaction {
                Orders.selectAll().toList().map { toOrder(it) }
            }
        }
    }


    override suspend fun insert(order: Order): InsertStatement<Number> {
        return withContext(Dispatchers.IO) {
            transaction {
                Orders.insert {
                    it[orderId] = order.orderId
                    it[productName] = order.productName
                    it[amount] = order.amount
                    it[customerName] = order.customerName
                    it[address] = order.address
                    it[lastModified] = order.lastModified
                    it[state] = order.state
                }
            }
        }
    }

    override suspend fun getById(orderId: String): Order? {
        return withContext(Dispatchers.IO) {
            transaction {
                Orders.select {Orders.orderId eq orderId}
                    .map { toOrder(it) }
                    .firstOrNull()
            }
        }
    }


    override suspend fun update(order: Order): Int {
        return withContext(Dispatchers.IO) {
            transaction {
                Orders.update({ Orders.orderId eq order.orderId})  {
                    it[orderId] = order.orderId
                    it[productName] = order.productName
                    it[amount] = order.amount
                    it[customerName] = order.customerName
                    it[address] = order.address
                    it[lastModified] = order.lastModified
                    it[state] = order.state
                }
            }
        }
    }

    override suspend fun delete(orderId: String): Int {
        return withContext(Dispatchers.IO) {
            transaction {
                Orders.deleteWhere { Orders.orderId eq orderId }
            }
        }
    }

    override suspend fun getByCustomerName(customerName: String): List<Order>? {
        return withContext(Dispatchers.IO) {
            transaction {
                return@transaction Orders
                    .select { Orders.customerName eq customerName }
                    .toList()
                    .map { toOrder(it) }
            }
        }
    }

    override suspend fun getReadyToPick(): List<Order>? {
        return withContext(Dispatchers.IO) {
            transaction {
                return@transaction Orders
                    .select { Orders.state eq OrderState.readyToPick.toString() }
                    .toList()
                    .map { toOrder(it) }
            }
        }
    }

    private fun toOrder(row: ResultRow): Order =
        Order(
            orderId = row[Orders.orderId],
            productName = row[Orders.productName],
            amount = row[Orders.amount],
            customerName = row[Orders.customerName],
            address = row[Orders.address],
            lastModified = row[Orders.lastModified],
            state = row[Orders.state]
        )
}
