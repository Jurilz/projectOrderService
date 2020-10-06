package com.orderService.dependencyInjection

import com.orderService.services.DefaultOrderService
import com.orderService.services.OrderService
import org.koin.dsl.module

val serviceModule = module {
    single<OrderService> {
        DefaultOrderService(get())
    }
}