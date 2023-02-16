package com.lukinhasssss.admin.catalogo.domain.event

@FunctionalInterface
interface DomainEventPublisher {
    fun publishEvent(event: DomainEvent)
}
