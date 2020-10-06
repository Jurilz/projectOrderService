package com.orderService.commands

open class OrderCommand(
    val orderId: String,
    val productName: String,
    val customerName: String,
    val address: String,
    val lastModified: String,
    val state: String
): Command() {}