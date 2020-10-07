package com.orderService.repository

import com.orderService.domain.Order
import com.orderService.events.Event
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoOrderReadRepository(database: CoroutineDatabase): OrderReadRepository {

    private val orderCollection: CoroutineCollection<Order> = database.getCollection()

    override suspend fun getAll(): List<Order> {
        return orderCollection.find().toList()
    }

    override suspend fun insert(order: Order) {
        orderCollection.insertOne(order)
    }

    override suspend fun getById(orderId: String): Order? {
       return orderCollection.findOne(Order::orderId eq orderId)
    }
}