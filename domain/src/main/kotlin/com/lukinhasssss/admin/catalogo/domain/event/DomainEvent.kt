package com.lukinhasssss.admin.catalogo.domain.event

import java.io.Serializable
import java.time.Instant

interface DomainEvent : Serializable {
    val occurredOn: Instant
}
