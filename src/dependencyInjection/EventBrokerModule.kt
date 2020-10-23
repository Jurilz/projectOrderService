package com.orderService.dependencyInjection

import com.orderService.messages.DefaultMessageBroker
import com.orderService.messages.MessageBroker
import org.koin.dsl.module

val eventBrokerModule = module {
    single<MessageBroker> {
        DefaultMessageBroker()
    }
}