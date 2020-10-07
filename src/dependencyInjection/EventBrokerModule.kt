package com.orderService.dependencyInjection

import com.orderService.messages.DefaultEventBroker
import com.orderService.messages.EventBroker
import org.koin.dsl.module

val eventBrokerModule = module {
    single<EventBroker> {
        DefaultEventBroker()
    }
}