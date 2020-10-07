package com.orderService.routing

import com.orderService.commands.*
import com.orderService.domain.Order
import com.orderService.services.OrderService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.orderRoutes() {

    val commandHandler: CommandHandler by inject()
    val orderService: OrderService by inject()

    route("/orders") {
        get {
            call.respond(orderService.getAll())
        }
        //authenticate {
        post {
            val createOrderCommand: CreateOrderCommand = call.receive<CreateOrderCommand>()
            commandHandler.handle(createOrderCommand)
            call.respond(createOrderCommand)
        }
        put {
            val updateOrderCommand: UpdateOrderCommand = call.receive<UpdateOrderCommand>()
            commandHandler.handle(updateOrderCommand)
            call.respond(updateOrderCommand)
        }
    }
    route("{id}") {
        delete {
            val id: String = call.parameters["id"]!!
            val deleted: Order? = orderService.getById(id)
            if (deleted != null) {
                val deleteOrderCommand: DeleteOrderCommand = buildDeleteOrderCommand(deleted)
                commandHandler.handle(deleteOrderCommand)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

fun buildDeleteOrderCommand(deleted: Order): DeleteOrderCommand {
    val orderCommand = OrderCommand(
        orderId = deleted.orderId,
        productName = deleted.product,
        amount = deleted.amount,
        customerName = deleted.customerName,
        address = deleted.address,
        lastModified = deleted.latModified,
        state = deleted.state
    )
    return DeleteOrderCommand(orderCommand)
}
