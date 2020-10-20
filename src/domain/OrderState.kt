package com.orderService.domain

enum class OrderState {
    pending,
    processedByOrderService,
    processedByWarehouse,
    readyToPick,
    beingDelivered,
    cancelationProccedByOrderService,
    cancelationProccedByWarehouse,
    canceledInDelivery,
    canceledByWarehouse
}