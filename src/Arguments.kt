package com.orderService

import com.apurebase.arkenv.Arkenv
import com.apurebase.arkenv.argument

/**
 * Contains the arguments of this application.
 */

object Arguments: Arkenv() {

    val mongoHost by argument<String>()

    val mongoPort by argument<String>() {
        defaultValue = { "27017" }
    }

    val eventDatabaseName by argument<String>() {
        defaultValue = { "orderEvents" }
    }

    val postgresHost by argument<String>()

    val postgresPort by argument<String>() {
        defaultValue = { "5432" }
    }

    val orderDatabaseName by argument<String>() {
        defaultValue = { "orders" }
    }

    val postgresUser by argument<String>(){
        defaultValue = { "postgres" }
    }

    val postgresPassword by argument<String>() {
        defaultValue = { "postgres" }
    }

    val rabbitHost by argument<String>() {
        defaultValue = { "rabbitmq" }
    }

    val orderSecret by argument<String>()
}