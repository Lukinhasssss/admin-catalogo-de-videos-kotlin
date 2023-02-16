package com.lukinhasssss.admin.catalogo.domain.event

@FunctionalInterface
interface DomainEventPublisher {
    fun <T : DomainEvent> publishEvent(event: T)
}
