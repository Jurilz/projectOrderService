package com.orderService.routing

import com.orderService.commands.CreateOrderCommand
import com.orderService.services.DefaultOrderService
import com.orderService.services.OrderService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Routing.orderRoutes() {

    val orderService: OrderService by inject()

    route("/orders") {
        //authenticate {
        post{
            val createOrderCommand: CreateOrderCommand = call.receive<CreateOrderCommand>()

            call.respond(createOrderCommand)
        }
    }


}