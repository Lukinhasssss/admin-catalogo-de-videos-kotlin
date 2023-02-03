package com.lukinhasssss.admin.catalogo.infrastructure.services.local

import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.infrastructure.services.StorageService

class InMemoryStorageService(
    private val storage: HashMap<String, Resource> = hashMapOf()
) : StorageService {

    override fun store(name: String, resource: Resource) {
        storage[name] = resource
    }

    override fun list(prefix: String): List<String> =
        storage.keys.filter { it.startsWith(prefix) }

    override fun deleteAll(names: Collection<String>) =
        names.forEach(storage::remove)

    override fun get(name: String): Resource? =
        storage[name]

    fun storage(): MutableMap<String, Resource> = storage

    fun reset() = storage.clear()
}
