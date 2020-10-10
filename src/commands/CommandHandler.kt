package com.orderService.commands

import com.orderService.events.OrderEvent
import com.orderService.messages.Subscriber

interface CommandHandler: Subscriber {

    suspend fun handle(command: Command)

    suspend fun deleteOrder(command: DeleteOrderCommand)

    suspend fun updateOrder(command: UpdateOrderCommand)

    suspend fun addOrder(command: CreateOrderCommand)

    fun buildOrderEvent(orderCommand: OrderCommand): OrderEvent

    fun determineNewState(orderCommand: OrderCommand): String


}