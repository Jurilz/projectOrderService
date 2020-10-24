package com.orderService.rabbitMQ

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.orderService.commands.Command
import com.orderService.events.Event
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.messages.MessageBroker
import com.orderService.messages.MessageTopic
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DefaultRabbitMQProvider(private val messageBroker: MessageBroker): RabbitProvider {

    private val mapper: ObjectMapper = jacksonObjectMapper()
    private val connectionFactory: ConnectionFactory
    private val channel: Channel
    private val deliverEventCallback: DeliverCallback
    private val cancelCallback: CancelCallback

    private val GENERAL_EXCHANGE = "generalExchange"
    private val TYPE_DIRECT = "direct"
    private val ORDER_COMMAND_QUEUE = "orderCommandQueue"
    private val ORDER_EVENT_QUEUE = "orderEventQueue"
    private val ORDER_COMMAND_KEY = "orderCommand"
    private val ORDER_EVENT_KEY = "orderEvent"

    init {
        connectionFactory = ConnectionFactory()
        //TODO: look at this
        connectionFactory.host = "rabbitmq"
        val newConnection: Connection = this.connectionFactory.newConnection()
        channel = newConnection.createChannel()
        channel.exchangeDeclare(GENERAL_EXCHANGE, TYPE_DIRECT, true)

        deliverEventCallback = DeliverCallback{
                _: String?,
                message: Delivery? ->
            val event: Event = mapper.readValue(message!!.body)
            println("Consuming Message \n ${String(message.body)}")
            GlobalScope.async {
                // TODO: wtf ???
                when(event) {
                    is OrderUpdatedEvent -> messageBroker.publishEvent(MessageTopic.ORDER_ORCHESTRATOR, event)
                    else -> messageBroker.publishEvent(MessageTopic.EVENT_HANDLER, event)
                }
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

    override suspend fun handleEvent(event: Event) {
        withContext(Dispatchers.IO) {
            channel.basicPublish(
                GENERAL_EXCHANGE,
                ORDER_EVENT_KEY,
                null,
                mapper.writeValueAsBytes(event)
            )
        }
    }

    override suspend fun handleCommand(command: Command) {
        withContext(Dispatchers.IO) {
            channel.basicPublish(
                GENERAL_EXCHANGE,
                ORDER_COMMAND_KEY,
                null,
                mapper.writeValueAsBytes(command)
            )
        }
    }
}