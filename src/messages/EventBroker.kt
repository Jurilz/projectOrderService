package com.orderService.messages

import com.orderService.commands.Command
import com.orderService.events.Event

interface EventBroker {

    fun subscribe(topic: EventTopic, subscriber: Subscriber)

    suspend fun publishEvent(topic: EventTopic, event: Event)

    suspend fun publishCommand(topic: EventTopic, command: Command)

    fun getOrCreateSubscriberList(topic: EventTopic): ArrayList<Subscriber>
}