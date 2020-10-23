package com.orderService.domain

import com.orderService.commands.OrderCommand

data class Order(
    val orderId: String,
    val productName: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val lastModified: String,
    val state: String
)

fun Order.buildOrderCommand(): OrderCommand {
    return OrderCommand(
        orderId = this.orderId,
        productName = this.productName,
        amount = this.amount,
        customerName = this.customerName,
        address = this.customerName,
        lastModified = this.lastModified,
        state = this.state
    )
}