package uniks.de.microservices_project.events.palletEvents

import com.orderService.events.Event

class PalletUpdatedEvent(val palletEvent: PalletEvent): Event()