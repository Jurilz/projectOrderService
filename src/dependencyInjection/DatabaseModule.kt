package com.orderService.dependencyInjection

import com.orderService.Arguments
import com.orderService.utils.loadDockerSecrets
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
            .createClient("mongodb://${Arguments.mongoHost}:${Arguments.mongoPort}")
            .coroutine
            .getDatabase(Arguments.eventDatabaseName)
//            .createClient()
//            .createClient("mongodb://mongo:27017")


    }

    single {
        Database
            .connect(
                url = "jdbc:postgresql://${Arguments.postgresHost}:${Arguments.postgresPort}/${Arguments.orderDatabaseName}",
                driver = "org.postgresql.Driver",
//                user = Arguments.postgresUser,
//                password =  Arguments.postgresPassword)
                user = loadDockerSecrets(Arguments.orderSecret)["POSTGRES_USER"]!!,
                password =  loadDockerSecrets(Arguments.orderSecret)["POSTGRES_PASSWORD"]!!)
//            .connect("jdbc:postgresql://localhost:5432/orders", driver = "org.postgresql.Driver",
//                user = "postgres", password = "postgres")
    }
}