package com.orderService.dependencyInjection

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
//            .createClient()
            .createClient("mongodb://mongo:27017")
            .coroutine
            .getDatabase("orderEvents")
    }

    single {
        Database
            .connect("jdbc:postgresql://postgres:5432/orders", driver = "org.postgresql.Driver",
                user = "postgres", password = "postgres")
//            .connect("jdbc:postgresql://localhost:5432/orders", driver = "org.postgresql.Driver",
//                user = "postgres", password = "postgres")
    }
}