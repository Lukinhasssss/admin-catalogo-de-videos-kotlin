package com.lukinhasssss.admin.catalogo.infrastructure.services

interface EventService {
    fun send(event: Any)
}
