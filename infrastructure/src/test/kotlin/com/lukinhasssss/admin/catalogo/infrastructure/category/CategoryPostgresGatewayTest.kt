package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.infrastructure.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@PostgresGatewayTest
class CategoryPostgresGatewayTest {

    @Autowired
    private lateinit var categoryGateway: CategoryPostgresGateway

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun testInjectedDependencies() {
        assertNotNull(categoryGateway)
        assertNotNull(categoryRepository)
    }
}
