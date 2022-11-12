package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.infrastructure.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory("filme", null, expectedIsActive)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory))

        assertEquals(1, categoryRepository.count())

        val anUpdatedCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive)

        val actualCategory = categoryGateway.update(anUpdatedCategory)

        assertEquals(1, categoryRepository.count())

        with(actualCategory) {
            assertEquals(aCategory.id.value, id.value)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(aCategory.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }

        val actualEntity = categoryRepository.findById(aCategory.id.value).get()

        with(actualEntity) {
            assertEquals(aCategory.id.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(aCategory.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        val aCategory = Category.newCategory("Filmes", null, true)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory))

        assertEquals(1, categoryRepository.count())

        categoryGateway.deleteById(aCategory.id)

        assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAnInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        assertEquals(0, categoryRepository.count())

        categoryGateway.deleteById(CategoryID.from("invalidId"))

        assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindbyId_shouldReturnACategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory))

        assertEquals(1, categoryRepository.count())

        val actualCategory = categoryGateway.findById(aCategory.id)

        assertEquals(1, categoryRepository.count())

        with(actualCategory!!) {
            assertEquals(aCategory.id.value, id.value)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertEquals(aCategory.updatedAt, updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnNull() {
        assertEquals(0, categoryRepository.count())

        val actualCategory = categoryGateway.findById(CategoryID.from("idNotStored"))

        assertNull(actualCategory)
    }
}
