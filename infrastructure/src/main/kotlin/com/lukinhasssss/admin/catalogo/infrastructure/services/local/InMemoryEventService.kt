package com.lukinhasssss.admin.catalogo.infrastructure.services.local

import com.lukinhasssss.admin.catalogo.infrastructure.services.EventService
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger

class InMemoryEventService : EventService {
    override fun send(event: Any) =
        Logger.info(message = "Event was observed", payload = event)
}
