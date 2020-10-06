package com.orderService.repository

import com.orderService.events.Event

interface EventWriteRepository {

    suspend fun getAll(): List<Event>

    suspend fun insert(event: Event)
}