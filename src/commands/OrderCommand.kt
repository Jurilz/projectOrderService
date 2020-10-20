package com.orderService.commands

import com.orderService.events.orderEvents.OrderEvent

open class OrderCommand(
    val orderId: String,
    val productName: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val lastModified: String,
    val state: String
): Command() {}

fun OrderCommand.buildOrderEvent(state: String): OrderEvent {
    return OrderEvent(
        orderId = this.orderId,
        productName = this.productName,
        address = this.address,
        amount = this.amount,
        customerName = this.customerName,
        state = state
    )
}