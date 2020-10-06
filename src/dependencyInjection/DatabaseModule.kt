package com.orderService.dependencyInjection

import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
            .createClient()
            .coroutine
            .getDatabase("")
    }
}