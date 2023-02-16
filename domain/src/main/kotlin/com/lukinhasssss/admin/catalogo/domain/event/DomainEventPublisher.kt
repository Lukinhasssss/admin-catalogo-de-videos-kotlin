package com.lukinhasssss.admin.catalogo.domain.event

@FunctionalInterface
interface DomainEventPublisher<T : DomainEvent> {
    fun publishEvent(event: T)
}
