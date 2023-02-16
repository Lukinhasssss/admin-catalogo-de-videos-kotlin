package com.lukinhasssss.admin.catalogo.domain

import com.lukinhasssss.admin.catalogo.domain.event.DomainEvent
import com.lukinhasssss.admin.catalogo.domain.event.DomainEventPublisher
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler

abstract class Entity<ID : Identifier>(
    open val id: ID,
    open val domainEvents: MutableList<DomainEvent> = mutableListOf()
) {

    abstract fun validate(handler: ValidationHandler)

    fun publishDomainEvents(publisher: DomainEventPublisher<DomainEvent>) {
        domainEvents.forEach { publisher.publishEvent(it) }
        domainEvents.clear()
    }

    fun registerEvent(event: DomainEvent) = domainEvents.add(event)
}
