package com.orderService.events.orderEvents

import com.orderService.commands.OrderCommand
import com.orderService.events.Event

class OrderEvent(
    val orderId: String,
    val productName: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val state: String
): Event()

fun OrderEvent.buildOrderEvent(state: String): OrderCommand {
    return OrderCommand(
        orderId = this.orderId,
        productName = this.productName,
        customerName = this.customerName,
        amount = this.amount,
        address = this.address,
        state = state,
        lastModified = this.lastModified
    )
}