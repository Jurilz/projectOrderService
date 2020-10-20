package com.orderService.orchestrator

import com.orderService.commands.CreateOrderCommand
import com.orderService.messages.EventBroker

class DefaultOrderOrchestrator(private val eventBroker: EventBroker): OrderOrchestrator {

    override fun orchestrateOrderPlacementSaga(createOrderCommand: CreateOrderCommand) {
        TODO("Not yet implemented")
    }
}