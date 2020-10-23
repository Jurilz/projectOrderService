package com.orderService.messages

import com.orderService.commands.Command
import com.orderService.events.Event

interface MessageBroker {

    fun subscribe(topic: MessageTopic, subscriber: Subscriber)

    suspend fun publishEvent(topic: MessageTopic, event: Event)

    suspend fun publishCommand(topic: MessageTopic, command: Command)

    fun getOrCreateSubscriberList(topic: MessageTopic): ArrayList<Subscriber>
}