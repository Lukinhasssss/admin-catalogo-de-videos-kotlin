package com.lukinhasssss.admin.catalogo.application.category.retrieve.list

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ListCategoriesUseCaseTest {

    @InjectMockKs
    private lateinit var useCase: DefaultListCategoriesUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() { clearAllMocks() }

    @Test
    fun givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        val categories = listOf(
            Category.newCategory("Filmes", null, true),
            Category.newCategory("Series", null, true)
        )

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, categories.size.toLong(), categories)

        val expectedItemsCount = 2
        val expectedResult = expectedPagination.map { CategoryListOutput.from(it) }

        every { categoryGateway.findAll(aQuery) } returns expectedPagination

        val actualResult = useCase.execute(aQuery)

        with(actualResult) {
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedResult, this)
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(categories.size.toLong(), total)
        }
    }

    @Test
    fun givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        val categories = emptyList<Category>()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, categories.size.toLong(), categories)

        val expectedItemsCount = 0
        val expectedResult = expectedPagination.map { CategoryListOutput.from(it) }

        every { categoryGateway.findAll(aQuery) } returns expectedPagination

        val actualResult = useCase.execute(aQuery)

        with(actualResult) {
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedResult, this)
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(categories.size.toLong(), total)
        }
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_thenReturnException() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "GatewayError"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        every { categoryGateway.findAll(aQuery) } throws IllegalStateException(expectedErrorMessage)

        val actualException = assertThrows<IllegalStateException> { useCase.execute(aQuery) }

        assertEquals(expectedErrorMessage, actualException.message)
    }
}
