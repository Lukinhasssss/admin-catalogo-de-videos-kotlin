package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
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

    @Test
    fun givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 3L

        val filmes = Category.newCategory("Filmes", null, true)
        val series = Category.newCategory("Series", null, true)
        val animes = Category.newCategory("Animes", null, true)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAllAndFlush(
            listOf(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(animes)
            )
        )

        assertEquals(3, categoryRepository.count())

        val query = SearchQuery(page = 0, perPage = 1, terms = "", sort = "name", direction = "asc")
        val actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedPerPage, items.size)
            assertEquals(animes.id.value, items.first().id.value)
        }
    }

    @Test
    fun givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 0L

        assertEquals(0, categoryRepository.count())

        val query = SearchQuery(page = 0, perPage = 1, terms = "", sort = "name", direction = "asc")
        val actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(0, items.size)
        }
    }

    @Test
    fun givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 3L

        val filmes = Category.newCategory("Filmes", null, true)
        val series = Category.newCategory("Series", null, true)
        val animes = Category.newCategory("Animes", null, true)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAllAndFlush(
            listOf(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(animes)
            )
        )

        assertEquals(3, categoryRepository.count())

        var query = SearchQuery(page = 0, perPage = 1, terms = "", sort = "name", direction = "asc")
        var actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedPerPage, items.size)
            assertEquals(animes.id.value, items.first().id.value)
        }

        expectedPage = 1

        query = SearchQuery(page = 1, perPage = 1, terms = "", sort = "name", direction = "asc")
        actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedPerPage, items.size)
            assertEquals(filmes.id.value, items.first().id.value)
        }

        expectedPage = 2

        query = SearchQuery(page = 2, perPage = 1, terms = "", sort = "name", direction = "asc")
        actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedPerPage, items.size)
            assertEquals(series.id.value, items.first().id.value)
        }
    }

    @Test
    fun givenPrePersistedCategoriesAndFilAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 1L

        val filmes = Category.newCategory("Filmes", null, true)
        val series = Category.newCategory("Series", null, true)
        val animes = Category.newCategory("Animes", null, true)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAllAndFlush(
            listOf(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(animes)
            )
        )

        assertEquals(3, categoryRepository.count())

        val query = SearchQuery(page = 0, perPage = 1, terms = "fil", sort = "name", direction = "asc")
        val actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedPerPage, items.size)
            assertEquals(filmes.id.value, items.first().id.value)
        }
    }

    @Test
    fun givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 1L

        val filmes = Category.newCategory("Filmes", "Os melhores filmes", true)
        val series = Category.newCategory("Series", "A categoria mais assistida", true)
        val animes = Category.newCategory("Animes", "Os animes mais assistidos", true)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAllAndFlush(
            listOf(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(animes)
            )
        )

        assertEquals(3, categoryRepository.count())

        val query = SearchQuery(page = 0, perPage = 1, terms = "MAIS ASSISTIDA", sort = "name", direction = "asc")
        val actualResult = categoryGateway.findAll(query)

        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedPerPage, items.size)
            assertEquals(series.id.value, items.first().id.value)
        }
    }
}
