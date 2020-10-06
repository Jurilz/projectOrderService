package com.orderService.services

import com.orderService.commands.Command
import com.orderService.commands.CreateOrderCommand
import com.orderService.commands.OrderCommand
import com.orderService.events.CreateOrderEvent
import com.orderService.events.Event
import com.orderService.events.OrderEvent
import com.orderService.repository.EventWriteRepository
import com.rabbitmq.client.Channel

class DefaultOrderService(val eventWriteRepository: EventWriteRepository): OrderService {



    private val channel: Channel = ra

    suspend fun handle(command: Command) {
        when(command) {
            is CreateOrderCommand -> addOrder(command)
        }
    }

    suspend fun addOrder(command: CreateOrderCommand) {
        val orderEvent: OrderEvent = buildOrderEvent(command.orderCommand)
        val createOrderEvent = CreateOrderEvent(orderEvent)
        storeAndEmitOrderEvent(createOrderEvent)
    }

    private suspend fun storeAndEmitOrderEvent(event: Event) {
        eventWriteRepository.insert(event)
        channel.basicPublish(
        )
    }

    private fun buildOrderEvent(orderCommand: OrderCommand): OrderEvent {
        val newState: String = determineNewState()

        val orderEvent = OrderEvent(
            orderId = orderCommand.orderId,
            productName = orderCommand.productName,
            customerName = orderCommand.customerName,
            address = orderCommand.address,
            state = newState
        )
        orderEvent.lastModified = orderCommand.lastModified
        return orderEvent
    }

}

