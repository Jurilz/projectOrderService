package com.orderService

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.orderService.commands.CommandHandler
import com.orderService.dependencyInjection.*
import com.orderService.events.EventHandler
import com.orderService.messages.MessageBroker
import com.orderService.messages.MessageTopic
import com.orderService.orchestrator.OrderOrchestrator
import com.orderService.rabbitMQ.RabbitProvider
import com.orderService.routing.orderRoutes
import com.orderService.services.OrderService
import io.ktor.jackson.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Koin){
        modules(
            repositoryModule +
                    databaseModule +
                    handlerModule +
                    eventBrokerModule +
                    projectorModule +
                    rabbitModule +
                    serviceModule +
                    orchestratorModule)
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DoubleReceive)

    val client = HttpClient(Apache) {
    }

    initializeEventBroker()

    routing {
        orderRoutes()
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

fun Application.initializeEventBroker() {
    val messageBroker: MessageBroker by inject()
    val rabbitMQProvider: RabbitProvider by inject()
    val eventHandler: EventHandler by inject()
    val orderService: OrderService by inject()
    val commandHandler: CommandHandler by inject()
    val orderOrchestrator: OrderOrchestrator by inject()
    messageBroker.subscribe(MessageTopic.COMMAND_HANDLER, commandHandler)
    messageBroker.subscribe(MessageTopic.ORDER_SERVICE, orderService)
    messageBroker.subscribe(MessageTopic.PUBLISH_ORDER, rabbitMQProvider)
    messageBroker.subscribe(MessageTopic.EVENT_HANDLER, eventHandler)
    messageBroker.subscribe(MessageTopic.ORDER_ORCHESTRATOR, orderOrchestrator)

}

