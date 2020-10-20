package uniks.de.microservices_project.events.palletEvents

import com.orderService.events.Event

class PalletEvent(
    val palletId: String,
    val product: String,
    val amount: Int,
    val priority: String,
    val userId: String,
    val storageLocation: String?,
    val state: String
): Event()