package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.infrastructure.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@PostgresGatewayTest
class CategoryPostgresGatewayTest {

    @Autowired
    private lateinit var categoryGateway: CategoryPostgresGateway

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        assertEquals(0, categoryRepository.count())

        val actualCategory = categoryGateway.create(aCategory)

        assertEquals(1, categoryRepository.count())

        with(actualCategory) {
            assertEquals(aCategory.id.value, id.value)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertEquals(aCategory.updatedAt, updatedAt)
            assertNull(deletedAt)
        }

        val actualEntity = categoryRepository.findById(aCategory.id.value).get()

        with(actualEntity) {
            assertEquals(aCategory.id.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertEquals(aCategory.updatedAt, updatedAt)
            assertNull(deletedAt)
        }
    }
}
