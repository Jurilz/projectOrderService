package com.orderService.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.orderService.utils.Util
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderEvent::class, name = "OrderEvent"),
    JsonSubTypes.Type(value = CreateOrderEvent::class, name = "CreateOrderEvent")
)
abstract class Event {
    var eventId: String = (UUID.randomUUID()).toString()
    var lastModified: String = Util.getCurrentDate()
}