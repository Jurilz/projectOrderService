package com.orderService.rabbitMQ

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.orderService.events.Event
import com.orderService.messages.EventBroker
import com.orderService.messages.EventTopic
import com.orderService.messages.Subscriber
import com.rabbitmq.client.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DefaultRabbitMQProvider(private val eventBroker: EventBroker): RabbitProvider {

    private val mapper: ObjectMapper = jacksonObjectMapper()
    private val connectionFactory: ConnectionFactory
    private val channel: Channel
    private val deliverEventCallback: DeliverCallback
    private val cancelCallback: CancelCallback

    private final val GENERAL_EXCHANGE = "generalExchange"
    private final val TYPE_DIRECT = "direct"
    private final val ORDER_COMMAND_QUEUE = "orderCommandQueue"
    private final val ORDER_EVENT_QUEUE = "orderEventQueue"
    private final val ORDER_COMMAND_KEY = "orderCommand"
    private final val ORDER_EVENT_KEY = "orderEvent"

    init {
        connectionFactory = ConnectionFactory()
        val newConnection: Connection = this.connectionFactory.newConnection()
        channel = newConnection.createChannel()
        channel.exchangeDeclare(GENERAL_EXCHANGE, TYPE_DIRECT, true)

        deliverEventCallback = DeliverCallback{
                _: String?,
                message: Delivery? ->
            val event: Event = mapper.readValue(message!!.body)
            println("Consuming Message \n ${String(message.body)}")
            GlobalScope.async {
                eventBroker.publish(EventTopic.ORDER_EVENT, event)
            }
        }
        
        cancelCallback = CancelCallback{
                consumerTag: String? -> println("Cancelled .. $consumerTag")
        }
        
        declareAndBindEventQueue()
    }

    override fun declareAndBindEventQueue() {
        channel.queueDeclare(ORDER_EVENT_QUEUE, true, false, true, emptyMap())
        channel.queueBind(ORDER_EVENT_QUEUE, GENERAL_EXCHANGE, ORDER_EVENT_KEY)
        channel.basicConsume(ORDER_EVENT_QUEUE, true, deliverEventCallback, cancelCallback)
    }

    override suspend fun handle(event: Event) {
        channel.basicPublish(
            GENERAL_EXCHANGE,
            ORDER_EVENT_KEY,
            null,
            mapper.writeValueAsBytes(event)
        )
    }
}