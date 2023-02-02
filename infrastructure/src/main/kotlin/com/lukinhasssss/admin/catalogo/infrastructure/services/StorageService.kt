package com.lukinhasssss.admin.catalogo.infrastructure.services

import com.lukinhasssss.admin.catalogo.domain.video.Resource

interface StorageService {

    fun store(name: String, resource: Resource)

    fun list(prefix: String): List<String>

    fun deleteAll(names: Collection<String>)

    fun get(name: String): Resource?
}
