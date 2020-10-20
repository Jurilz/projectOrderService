package com.orderService.events.orderEvents

import com.orderService.events.Event

class OrderUpdatedEvent(val orderEvent: OrderEvent): Event()