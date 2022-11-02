package com.lukinhasssss.admin.catalogo.domain

open class AggregateRoot<ID : Identifier>(id: ID) : Entity<ID>(id)
