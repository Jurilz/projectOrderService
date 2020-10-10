package com.orderService.database

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Orders: IntIdTable() {
    val orderId = varchar("orderId", 50).uniqueIndex()
    val productName = varchar("productName", 50)
    val amount = integer("amount")
    val customerName = varchar("customerName", 50)
    val address = varchar("address", 50)
    val latModified = varchar("latModified", 50)
    val state = varchar("state", 50)
    override val primaryKey = PrimaryKey(orderId)
}