package com.orderService.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.orderService.domain.Order
import com.orderService.domain.OrderState
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

//class MongoOrderReadRepository(database: CoroutineDatabase): OrderReadRepository {
//
//    private val orderCollection: CoroutineCollection<Order> = database.getCollection()
//
//    override suspend fun getAll(): List<Order> {
//        return orderCollection.find().toList()
//    }
//
//    override suspend fun insert(order: Order) {
//        orderCollection.insertOne(order)
//    }
//
//    override suspend fun getById(orderId: String): Order? {
//       return orderCollection.findOne(Order::orderId eq orderId)
//    }
//
//    override suspend fun update(order: Order): UpdateResult {
//        return orderCollection.updateOne(Order::orderId eq order.orderId, order)
//    }
//
//    override suspend fun delete(orderId: String): DeleteResult {
//        return orderCollection.deleteOne(Order::orderId eq orderId)
//    }
//
//    override suspend fun getByCustomerName(customerName: String): List<Order>? {
//        return orderCollection.find(Order::customerName eq customerName).toList()
//    }
//
//    override suspend fun getReadyToPick(): List<Order>? {
//        return orderCollection.find(Order::state eq OrderState.readyToPick.toString()).toList()
//    }
//}