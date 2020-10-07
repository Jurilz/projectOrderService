package com.orderService.dependencyInjection

import com.orderService.rabbitMQ.DefaultRabbitMQProvider
import com.orderService.rabbitMQ.RabbitProvider
import org.koin.dsl.module

val rabbitModule = module {
    single<RabbitProvider>{
        DefaultRabbitMQProvider(get())
    }
}
