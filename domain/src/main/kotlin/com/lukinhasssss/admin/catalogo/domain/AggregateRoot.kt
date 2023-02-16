package com.lukinhasssss.admin.catalogo.domain

import com.lukinhasssss.admin.catalogo.domain.event.DomainEvent

abstract class AggregateRoot<ID : Identifier>(
    id: ID,
    domainEvents: MutableList<DomainEvent> = mutableListOf()
) : Entity<ID>(id, domainEvents)
