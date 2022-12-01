package com.lukinhasssss.admin.catalogo.application.genre.retrive.list

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ListGenreUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultListGenreUseCase

    @MockK
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        // given
        val genres = listOf(
            Genre.newGenre("Ação", true),
            Genre.newGenre("Aventura", true)
        )

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = genres.size.toLong()
        val expectedItems = genres.map { GenreListOutput.from(it) }

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, expectedTotal, genres)

        every { genreGateway.findAll(aQuery) } returns expectedPagination

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }

        verify(exactly = 1) { genreGateway.findAll(aQuery) }
    }

    @Test
    fun givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        // given
        val genres = listOf<Genre>()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0L
        val expectedItems = listOf<GenreListOutput>()

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, expectedTotal, genres)

        every { genreGateway.findAll(aQuery) } returns expectedPagination

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }

        verify(exactly = 1) { genreGateway.findAll(aQuery) }
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_thenReturnException() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway error"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        every { genreGateway.findAll(aQuery) } throws IllegalStateException(expectedErrorMessage)

        // when
        val actualException = assertThrows<IllegalStateException> { useCase.execute(aQuery) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { genreGateway.findAll(aQuery) }
    }
}
