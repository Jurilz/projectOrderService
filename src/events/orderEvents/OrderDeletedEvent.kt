package com.orderService.events.orderEvents

import com.orderService.events.Event

class OrderDeletedEvent(val orderEvent: OrderEvent): Event()