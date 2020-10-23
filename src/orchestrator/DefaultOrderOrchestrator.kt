package com.orderService.orchestrator

import com.orderService.commands.*
import com.orderService.domain.OrderState
import com.orderService.events.Event
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.events.orderEvents.buildOrderEvent
import com.orderService.messages.MessageBroker
import com.orderService.messages.MessageTopic

class DefaultOrderOrchestrator(private val messageBroker: MessageBroker): OrderOrchestrator {

    override suspend fun handleCommand(command: Command) {
        when(command) {
            is CreateOrderCommand -> orchestrateOrderPlacementSaga(command)
            is DeleteOrderCommand -> orchestrateOrderCancellationSaga(command)
        }
    }

    override suspend fun handleEvent(event: Event) {
        when(event) {
            is OrderUpdatedEvent -> handleUpdateOrderEvent(event)
        }
    }

    override suspend fun orchestrateOrderPlacementSaga(createOrderCommand: CreateOrderCommand) {
        val validateOrderCommand = ValidateOrderCommand(createOrderCommand.orderCommand)
        messageBroker.publishCommand(MessageTopic.PUBLISH_ORDER, validateOrderCommand)
    }

    private suspend fun orchestrateOrderCancellationSaga(command: DeleteOrderCommand) {
        val cancelCommand = OrderCommand(
            orderId = command.orderCommand.orderId,
            productName = command.orderCommand.productName,
            amount = command.orderCommand.amount,
            customerName = command.orderCommand.customerName,
            address = command.orderCommand.address,
            lastModified = command.orderCommand.lastModified,
            state = determineCancellationState(command.orderCommand)
        )
        val cancelOrderCommand = CancelOrderCommand(cancelCommand)
        messageBroker.publishCommand(MessageTopic.PUBLISH_ORDER, cancelOrderCommand)
    }

    private suspend fun handleUpdateOrderEvent(event: OrderUpdatedEvent) {
        when(event.orderEvent.state) {
            OrderState.cancelationProccedByWarehouse.toString() -> deleteOrder(event)
            else -> updateOrder(event)
        }
    }

    private suspend fun deleteOrder(event: OrderUpdatedEvent) {
        val orderDeletionEvent = OrderDeletedEvent(event.orderEvent)
        messageBroker.publishEvent(MessageTopic.ORDER_SERVICE, orderDeletionEvent)
    }

    private suspend fun updateOrder(event: OrderUpdatedEvent) {
        val orderCommand: OrderCommand = event.orderEvent.buildOrderEvent(event.orderEvent.state)
        val updateOrderCommand = UpdateOrderCommand(orderCommand)
        messageBroker.publishCommand(MessageTopic.ORDER_SERVICE, updateOrderCommand)
    }

    private fun determineCancellationState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.processedByWarehouse.toString() -> OrderState.cancelationProccedByOrderService.toString()
            OrderState.readyToPick.toString() -> OrderState.cancelationProccedByOrderService.toString()
            else -> orderCommand.state
        }
    }
}
