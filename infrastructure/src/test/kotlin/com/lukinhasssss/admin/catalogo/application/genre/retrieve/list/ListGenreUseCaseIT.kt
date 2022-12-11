package com.lukinhasssss.admin.catalogo.application.genre.retrieve.list

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.GenreListOutput
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.ListGenreUseCase
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ListGenreUseCaseIT {

    @Autowired
    private lateinit var useCase: ListGenreUseCase

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        // given
        val genres = listOf(
            Genre.newGenre("Ação", true),
            Genre.newGenre("Aventura", true)
        )

        genreRepository.saveAllAndFlush(genres.map { GenreJpaEntity.from(it) })

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = genres.size.toLong()
        val expectedItems = genres.map { GenreListOutput.from(it) }

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertTrue(
                expectedItems.size == items.size &&
                    expectedItems.containsAll(items)
            )
        }
    }

    @Test
    fun givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0L
        val expectedItems = listOf<GenreListOutput>()

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }
    }
}
