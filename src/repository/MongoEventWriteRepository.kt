package com.orderService.repository

import com.orderService.events.Event
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoEventWriteRepository(database: CoroutineDatabase): EventWriteRepository {

    private val eventCollection: CoroutineCollection<Event> = database.getCollection<Event>()

    override suspend fun getAll(): List<Event> {
        return eventCollection.find().toList()
    }

    override suspend fun insert(event: Event) {
        eventCollection.insertOne(event)
    }
}