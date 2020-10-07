package com.orderService.services

import com.orderService.domain.Order
import com.orderService.events.Event
import com.orderService.events.OrderCreatedEvent
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic
import com.orderService.messages.Subscriber
import com.orderService.repository.EventWriteRepository
import com.orderService.repository.OrderReadRepository

class DefaultOrderService(
    private val eventWriteRepository: EventWriteRepository,
    private val orderReadRepository: OrderReadRepository,
    private val eventBroker: EventBroker
): OrderService, Subscriber {

    init {
        eventBroker.subscribe(EventTopic.ORDER_SERVICE, this)
    }

    override suspend fun storeAndPublishOrderEvent(event: Event) {
        eventWriteRepository.insert(event)
        eventBroker.publish(EventTopic.PUBLISH_ORDER, event)
    }


    override suspend fun handle(event: Event) {
        when(event) {
            is OrderCreatedEvent -> storeAndPublishOrderEvent(event)
        }
    }

    override suspend fun getAll(): List<Order> {
        return orderReadRepository.getAll()
    }

    override suspend fun getById(orderId: String): Order? {
        return orderReadRepository.getById(orderId)
    }
}