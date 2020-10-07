package com.orderService.messages

import com.orderService.events.Event

interface EventBroker {

    fun subscribe(topic: EventTopic, subscriber: Subscriber)

    suspend fun publish(topic: EventTopic, event: Event)

    fun getOrCreateSubscriberList(topic: EventTopic): ArrayList<Subscriber>
}