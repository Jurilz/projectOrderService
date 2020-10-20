package com.orderService.orchestrator

import com.orderService.commands.*
import com.orderService.domain.OrderState
import com.orderService.events.Event
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.events.orderEvents.buildOrderEvent
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic

class DefaultOrderOrchestrator(private val eventBroker: EventBroker): OrderOrchestrator {

    override suspend fun handleEvent(event: Event) {
        when(event) {
            is OrderUpdatedEvent -> updateOrder(event)
        }
    }

    override suspend fun handleCommand(command: Command) {
        when(command) {
            is CreateOrderCommand -> orchestrateOrderPlacementSaga(command)
            is DeleteOrderCommand -> orchestrateOrderCancellationSaga(command)
        }
    }

    override suspend fun orchestrateOrderPlacementSaga(createOrderCommand: CreateOrderCommand) {
        val validateOrderCommand = ValidateOrderCommand(createOrderCommand.orderCommand)
        eventBroker.publishCommand(EventTopic.PUBLISH_ORDER, validateOrderCommand)
    }

    private suspend fun orchestrateOrderCancellationSaga(command: DeleteOrderCommand) {
        val cancelCommand = OrderCommand(
            orderId = command.orderCommand.orderId,
            productName = command.orderCommand.orderId,
            amount = command.orderCommand.amount,
            customerName = command.orderCommand.customerName,
            address = command.orderCommand.address,
            lastModified = command.orderCommand.lastModified,
            state = determineCancellationState(command.orderCommand)
        )
        val cancelOrderCommand = CancelOrderCommand(cancelCommand)
        eventBroker.publishCommand(EventTopic.PUBLISH_ORDER, cancelOrderCommand)
    }

    private suspend fun updateOrder(event: OrderUpdatedEvent) {
        val orderCommand: OrderCommand = event.orderEvent.buildOrderEvent(event.orderEvent.state)
        val updateOrderCommand = UpdateOrderCommand(orderCommand)
        eventBroker.publishCommand(EventTopic.ORDER_SERVICE, updateOrderCommand)
    }

    private fun determineCancellationState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.processedByWarehouse.toString() -> OrderState.cancelationProccedByOrderService.toString()
            OrderState.readyToPick.toString() -> OrderState.cancelationProccedByOrderService.toString()
            OrderState.beingDelivered.toString() -> OrderState.canceledInDelivery.toString()
            else -> orderCommand.state
        }
    }
}
