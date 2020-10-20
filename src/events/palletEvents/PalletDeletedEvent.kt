package uniks.de.microservices_project.events.palletEvents

import com.orderService.events.Event

class PalletDeletedEvent(val palletEvent: PalletEvent): Event()