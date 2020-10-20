package com.orderService.database

import org.jetbrains.exposed.sql.Table

object Orders: Table() {
    val orderId = varchar("orderId", 50)
    val productName = varchar("productName", 50)
    val amount = integer("amount")
    val customerName = varchar("customerName", 50)
    val address = varchar("address", 50)
    val lastModified = varchar("lastModified", 50)
    val state = varchar("state", 50)
    override val primaryKey = PrimaryKey(orderId)
}