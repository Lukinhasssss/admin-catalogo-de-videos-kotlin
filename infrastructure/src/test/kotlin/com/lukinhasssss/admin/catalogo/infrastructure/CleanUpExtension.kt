package com.lukinhasssss.admin.catalogo.infrastructure

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit.jupiter.SpringExtension

class CleanUpExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val repositories = SpringExtension.getApplicationContext(context)
            .getBeansOfType(CrudRepository::class.java)
            .values

        cleanUp(repositories)
    }

    private fun cleanUp(repositories: MutableCollection<CrudRepository<*, *>>) {
        repositories.forEach { it.deleteAll() }
    }
}
