package com.orderService.events

interface EventHandler {

    suspend fun handle(event: Event)
}