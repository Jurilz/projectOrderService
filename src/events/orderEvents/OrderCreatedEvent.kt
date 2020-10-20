package com.orderService.events.orderEvents

import com.orderService.events.Event

class OrderCreatedEvent(val orderEvent: OrderEvent): Event()