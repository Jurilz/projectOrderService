package uniks.de.microservices_project.events.palletEvents

import com.orderService.events.Event

class PalletCreatedEvent(val palletEvent: PalletEvent): Event()