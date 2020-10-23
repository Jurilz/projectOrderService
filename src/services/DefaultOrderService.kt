package com.orderService.services

import com.orderService.commands.*
import com.orderService.domain.Order
import com.orderService.domain.OrderState
import com.orderService.events.Event
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.messages.MessageBroker
import com.orderService.messages.MessageTopic
import com.orderService.repository.EventWriteRepository
import com.orderService.repository.OrderReadRepository

class DefaultOrderService(
    private val eventWriteRepository: EventWriteRepository,
    private val orderReadRepository: OrderReadRepository,
    private val messageBroker: MessageBroker
): OrderService {

    override suspend fun storeAndPublishOrderEvent(event: Event) {
        eventWriteRepository.insert(event)
        messageBroker.publishEvent(MessageTopic.PUBLISH_ORDER, event)
    }


    override suspend fun handleCommand(command: Command) {
        when(command) {
            is CreateOrderCommand -> handleCreateOrderCommand(command)
            is UpdateOrderCommand -> handleUpdateOrderCommand(command)
            is DeleteOrderCommand -> handleDeleteOrderCommand(command)
        }
    }

    override suspend fun handleEvent(event: Event) {
        when(event) {
            is OrderCreatedEvent -> storeAndPublishOrderEvent(event)
            is OrderUpdatedEvent -> storeAndPublishOrderEvent(event)
            is OrderDeletedEvent -> storeAndPublishOrderEvent(event)
        }
    }

    override suspend fun getAll(): List<Order> {
        return orderReadRepository.getAll()
    }

    override suspend fun getById(orderId: String): Order? {
        return orderReadRepository.getById(orderId)
    }

    override suspend fun getByCustomerName(customerName: String): List<Order>? {
        return orderReadRepository.getByCustomerName(customerName)
    }

    override suspend fun getReadyToPick(): List<Order>? {
        return orderReadRepository.getReadyToPick()
    }

    private suspend fun handleDeleteOrderCommand(command: DeleteOrderCommand) {
        val newState: String = determineNewState(command.orderCommand)
        val orderEvent: OrderEvent = command.orderCommand.buildOrderEvent(newState)
        val orderDeletedEvent = OrderDeletedEvent(orderEvent)
        eventWriteRepository.insert(orderDeletedEvent)

        when(command.orderCommand.state) {
            OrderState.readyToPick.toString() -> messageBroker.publishCommand(MessageTopic.ORDER_ORCHESTRATOR, command)
            OrderState.beingDelivered.toString() -> messageBroker.publishCommand(MessageTopic.ORDER_ORCHESTRATOR, command)
            else -> deleteOrder(command)
        }
    }

    private suspend fun deleteOrder(command: DeleteOrderCommand) {
        val newState: String = determineNewState(command.orderCommand)
        val deletedEvent: OrderEvent = command.orderCommand.buildOrderEvent(newState)
        val orderDeletedEvent = OrderDeletedEvent(deletedEvent)
        messageBroker.publishEvent(MessageTopic.EVENT_HANDLER, orderDeletedEvent)
    }

    private suspend fun handleCreateOrderCommand(command: CreateOrderCommand) {
        val newState: String = determineNewState(command.orderCommand)
        val orderEvent: OrderEvent = command.orderCommand.buildOrderEvent(newState)
        val orderCreatedEvent = OrderCreatedEvent(orderEvent)
        storeAndPublishOrderEvent(orderCreatedEvent)

        messageBroker.publishCommand(MessageTopic.ORDER_ORCHESTRATOR, command)
    }

    private suspend fun handleUpdateOrderCommand(command: UpdateOrderCommand) {
        val newState: String = determineNewState(command.orderCommand)
        val orderEvent: OrderEvent = command.orderCommand.buildOrderEvent(newState)
        val orderUpdatedEvent = OrderUpdatedEvent(orderEvent)
        eventWriteRepository.insert(orderUpdatedEvent)

        messageBroker.publishEvent(MessageTopic.EVENT_HANDLER, orderUpdatedEvent)
    }

    private fun determineNewState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.pending.toString() -> OrderState.processedByOrderService.toString()
            OrderState.processedByWarehouse.toString() -> OrderState.readyToPick.toString()
            else -> orderCommand.state
        }
    }
}