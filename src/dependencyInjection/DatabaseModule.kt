package com.orderService.dependencyInjection

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
            .createClient()
            .coroutine
            .getDatabase("orders")
    }

    single {
        Database
            .connect("jdbc:postgresql://localhost:5432/orders", driver = "org.postgresql.Driver",
                user = "postgres", password = "postgres")
    }
}