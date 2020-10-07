package com.orderService.dependencyInjection

import com.orderService.commands.DefaultCommandHandler
import com.orderService.commands.CommandHandler
import com.orderService.events.DefaultEventHandler
import com.orderService.events.EventHandler
import org.koin.dsl.module

val handlerModule = module {
    single<CommandHandler> {
        DefaultCommandHandler(get())
    }

    single<EventHandler> {
        DefaultEventHandler(get())
    }
}