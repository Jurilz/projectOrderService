package com.orderService.dependencyInjection

import com.orderService.repository.EventWriteRepository
import com.orderService.repository.MongoEventWriteRepository
import com.orderService.repository.MongoOrderReadRepository
import com.orderService.repository.OrderReadRepository
import org.koin.dsl.module

/**
 * The repository Koin module.
 * Uses MongoDB repositories.
 */
val repositoryModule = module {
    single<EventWriteRepository> {
        MongoEventWriteRepository(get())
    }

    single<OrderReadRepository> {
        MongoOrderReadRepository(get())
    }
}