package com.lukinhasssss.admin.catalogo.domain

abstract class AggregateRoot<ID : Identifier>(id: ID) : Entity<ID>(id)
