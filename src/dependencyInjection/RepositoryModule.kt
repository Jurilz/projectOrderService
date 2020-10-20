package com.orderService.dependencyInjection

import com.orderService.repository.*
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
        PostgresOrderReadRepository(get())
    }
}