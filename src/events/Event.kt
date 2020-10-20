package com.orderService.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.orderService.events.orderEvents.OrderCreatedEvent
import com.orderService.events.orderEvents.OrderDeletedEvent
import com.orderService.events.orderEvents.OrderEvent
import com.orderService.events.orderEvents.OrderUpdatedEvent
import com.orderService.utils.Util
import uniks.de.microservices_project.events.palletEvents.PalletCreatedEvent
import uniks.de.microservices_project.events.palletEvents.PalletDeletedEvent
import uniks.de.microservices_project.events.palletEvents.PalletEvent
import uniks.de.microservices_project.events.palletEvents.PalletUpdatedEvent
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderEvent::class, name = "OrderEvent"),
    JsonSubTypes.Type(value = OrderCreatedEvent::class, name = "OrderCreatedEvent"),
    JsonSubTypes.Type(value = OrderUpdatedEvent::class, name = "OrderUpdatedEvent"),
    JsonSubTypes.Type(value = OrderDeletedEvent::class, name = "OrderDeletedEvent"),
    JsonSubTypes.Type(value = PalletEvent::class, name = "PalletEvent"),
    JsonSubTypes.Type(value = PalletCreatedEvent::class, name = "PalletCreatedEvent"),
    JsonSubTypes.Type(value = PalletUpdatedEvent::class, name = "PalletUpdatedEvent"),
    JsonSubTypes.Type(value = PalletDeletedEvent::class, name = "PalletDeletedEvent")
)
abstract class Event {
    var eventId: String = (UUID.randomUUID()).toString()
    var lastModified: String = Util.getCurrentDate()
}