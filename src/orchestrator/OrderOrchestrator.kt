package com.orderService.orchestrator

import com.orderService.commands.CreateOrderCommand

interface OrderOrchestrator {

    fun orchestrateOrderPlacementSaga(createOrderCommand: CreateOrderCommand)
}