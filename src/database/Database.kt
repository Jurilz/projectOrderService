package com.orderService.database

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object Database {
    private const val EVENT_DATABASE = "events"

    private var client: CoroutineClient? = null

    fun getEventDatabase(databaseName: String): CoroutineDatabase {
        if (this.client == null) {
            client = KMongo.createClient().coroutine
        }
        return this.client!!.getDatabase(EVENT_DATABASE)
    }
}