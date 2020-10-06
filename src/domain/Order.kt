package com.orderService.domain

data class Order(
    val orderId: String,
    val product: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val latModified: String,
    val state: String
)
