package com.orderService.events

class OrderEvent(
    val orderId: String,
    val productName: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val state: String
): Event()