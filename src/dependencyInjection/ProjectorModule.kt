package com.orderService.dependencyInjection

import com.orderService.projectors.DefaultOrderProjector
import com.orderService.projectors.OrderProjector
import org.koin.dsl.module

val projectorModule = module {
    single<OrderProjector> {
        DefaultOrderProjector(get())
    }
}