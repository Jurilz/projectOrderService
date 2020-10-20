package com.orderService.dtos

import com.orderService.database.Orders
import com.orderService.database.Orders.orderId
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

//class Order(id: EntityID<Int>) : IntEntity(id) {
//    companion object: IntEntityClass<Order>(Orders)
//
//    val orderId by Orders.orderId
//    val productName by Orders.productName
//    val customerName by Orders.customerName
//    val address by Orders.address
//    val lastModified by Orders.lastModified
//    val state by Orders.state
//}