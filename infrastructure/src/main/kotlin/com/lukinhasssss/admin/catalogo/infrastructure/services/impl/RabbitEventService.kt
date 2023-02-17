package com.lukinhasssss.admin.catalogo.infrastructure.services.impl

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.services.EventService
import org.springframework.amqp.rabbit.core.RabbitOperations

class RabbitEventService(
    private val exchange: String,
    private val routingKey: String,
    private val ops: RabbitOperations
) : EventService {

    override fun send(event: Any) {
        ops.convertAndSend(exchange, routingKey, Json.writeValueAsString(event))
    }
}
