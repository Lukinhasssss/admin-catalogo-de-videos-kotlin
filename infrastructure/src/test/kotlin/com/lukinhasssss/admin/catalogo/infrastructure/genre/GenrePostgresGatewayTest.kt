package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.infrastructure.category.CategoryPostgresGateway
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@PostgresGatewayTest
class GenrePostgresGatewayTest {

    @Autowired
    private lateinit var categoryGateway: CategoryPostgresGateway

    @Autowired
    private lateinit var genreGateway: GenrePostgresGateway

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun testDependenciesInjected() {
        assertNotNull(categoryGateway)
        assertNotNull(genreGateway)
        assertNotNull(genreRepository)
    }
}
