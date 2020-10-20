package com.orderService.orchestrator

import com.orderService.commands.CreateOrderCommand
import com.orderService.messages.Subscriber

interface OrderOrchestrator: Subscriber {

    suspend fun orchestrateOrderPlacementSaga(createOrderCommand: CreateOrderCommand)
}