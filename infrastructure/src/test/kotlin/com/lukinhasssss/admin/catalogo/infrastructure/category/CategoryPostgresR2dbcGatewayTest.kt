package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.infrastructure.PostgresR2dbcGatewayTest
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryPostgresR2dbcGateway
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@PostgresR2dbcGatewayTest
class CategoryPostgresR2dbcGatewayTest {

    @Autowired
    private lateinit var categoryGateway: CategoryPostgresR2dbcGateway

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun testInjectedDependencies() {
        assertNotNull(categoryGateway)
        assertNotNull(categoryRepository)
    }
}
