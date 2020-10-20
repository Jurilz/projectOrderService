package com.orderService.dependencyInjection

import com.orderService.orchestrator.DefaultOrderOrchestrator
import com.orderService.orchestrator.OrderOrchestrator
import org.koin.dsl.module

val orchestratorModule = module {
    single<OrderOrchestrator> {
        DefaultOrderOrchestrator(get())
    }
}