package com.orderService.messages

import com.orderService.events.Event

class DefaultEventBroker: EventBroker {

    private val subscriberMap: HashMap<EventTopic, ArrayList<Subscriber>> = HashMap()

    override fun subscribe(topic: EventTopic, subscriber: Subscriber) {
        val subscriberList: ArrayList<Subscriber> = getOrCreateSubscriberList(topic)
        subscriberList.add(subscriber)
    }

    override suspend fun publish(topic: EventTopic, event: Event) {
        val subscribers = subscriberMap[topic]
        if (subscribers != null) {
            for (subscriber: Subscriber in subscribers) {
                subscriber.handle(event)
            }
        }
    }

    override fun getOrCreateSubscriberList(topic: EventTopic): ArrayList<Subscriber> {
        if (subscriberMap[topic] == null) {
            subscriberMap[topic] = ArrayList()
        }
        return subscriberMap[topic]!!
    }


}