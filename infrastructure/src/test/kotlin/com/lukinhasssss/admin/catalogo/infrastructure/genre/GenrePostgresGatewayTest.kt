package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.category.CategoryPostgresGateway
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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

    @Test
    fun givenAValidGenre_whenCallsCreateGenre_shouldPersisteGenre() {
        // given
        val filmes = categoryGateway.create(Category.newCategory("Filmes", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes.id)

        val aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        // when
        val actualGenre = genreGateway.create(aGenre)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, getCategoryIDs())
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersisteGenre() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        // when
        val actualGenre = genreGateway.create(aGenre)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, getCategoryIDs())
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersisteGenre() {
        // given
        val filmes = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val series = categoryGateway.create(Category.newCategory("Series", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes.id, series.id)

        val aGenre = Genre.newGenre("acao", expectedIsActive)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))

        assertEquals(1, genreRepository.count())

        // when
        val actualGenre = genreGateway.update(
            Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories)
        )

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(categories))
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersisteGenre() {
        // given
        val filmes = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val series = categoryGateway.create(Category.newCategory("Series", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes.id, series.id)

        val aGenre = Genre.newGenre("acao", expectedIsActive)
        aGenre.addCategories(listOf(filmes.id, series.id))

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))

        assertEquals(1, genreRepository.count())

        val aGenreUpdated = Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories)

        // when
        val actualGenre = genreGateway.update(aGenreUpdated)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(categories))
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersisteGenre() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()

        val aGenre = Genre.newGenre(expectedName, false)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))

        assertEquals(1, genreRepository.count())
        assertFalse(aGenre.isActive())
        assertNotNull(aGenre.deletedAt)

        val aGenreUpdated = Genre.with(aGenre).update(expectedName, expectedIsActive)

        // when
        val actualGenre = genreGateway.update(aGenreUpdated)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(categories))
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersisteGenre() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf<CategoryID>()

        val aGenre = Genre.newGenre(expectedName, true)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))

        assertEquals(1, genreRepository.count())
        assertTrue(aGenre.isActive())
        assertNull(aGenre.deletedAt)

        val aGenreUpdated = Genre.with(aGenre).update(expectedName, expectedIsActive)

        // when
        val actualGenre = genreGateway.update(aGenreUpdated)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNotNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, getCategoryIDs())
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        // given
        val aGenre = Genre.newGenre("Ação", true)

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))

        assertEquals(1, genreRepository.count())

        // when
        genreGateway.deleteById(aGenre.id)

        // then
        assertEquals(0, genreRepository.count())
    }

    @Test
    fun givenAnInvalidGenreId_whenCallsDeleteById_shouldDeleteGenre() {
        // given
        assertEquals(0, genreRepository.count())

        // when
        genreGateway.deleteById(GenreID.from("123"))

        // then
        assertEquals(0, genreRepository.count())
    }

    @Test
    fun givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        // given
        val filmes = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val series = categoryGateway.create(Category.newCategory("Series", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes.id, series.id)

        val aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories)

        val expectedId = aGenre.id

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))

        assertEquals(1, genreRepository.count())

        // when
        val actualGenre = genreGateway.findById(expectedId)

        // then

        with(actualGenre!!) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(categories))
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidGenreId_whenCallsFindById_shouldReturnNull() {
        // given
        val expectedId = GenreID.from("any")

        assertEquals(0, genreRepository.count())

        // when
        val actualGenre = genreGateway.findById(expectedId)

        // then
        assertNull(actualGenre)
    }

    @Test
    fun givenEmptyGenres_whenCallsFindAll_shouldReturnEmptyList() {
        // given
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTotal = 0

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = genreGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal.toLong(), total)
            assertEquals(expectedTotal, items.size)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "aç, 0, 10, 1, 1, Ação",
        "dr, 0, 10, 1, 1, Drama",
        "com, 0, 10, 1, 1, Comédia Romântica",
        "cien, 0, 10, 1, 1, Ficção Científica",
        "terr, 0, 10, 1, 1, Terror"
    )
    fun givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
        expectedTerms: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedGenreName: String
    ) {
        // given
        mockGenres()

        val expectedSort = "name"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = genreGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedGenreName, items[0].name)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "name, asc, 0, 10, 5, 5, Ação",
        "name, desc, 0, 10, 5, 5, Terror",
        "createdAt, asc, 0, 10, 5, 5, Comédia Romântica",
        "createdAt, desc, 0, 10, 5, 5, Ficção Científica"
    )
    fun givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
        expectedSort: String,
        expectedDirection: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedGenreName: String
    ) {
        // given
        mockGenres()

        val expectedTerms = ""

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = genreGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedGenreName, items[0].name)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "0, 2, 2, 5, Ação;Comédia Romântica",
        "1, 2, 2, 5, Drama;Ficção Científica",
        "2, 2, 1, 5, Terror"
    )
    fun givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedGenres: String
    ) {
        // given
        mockGenres()

        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = genreGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)

            for ((index, expectedGenreName) in expectedGenres.split(";").withIndex()) {
                val actualGenreName = items[index].name
                assertEquals(expectedGenreName, actualGenreName)
            }
        }
    }

    private fun mockGenres() {
        genreRepository.saveAllAndFlush(
            listOf(
                GenreJpaEntity.from(Genre.newGenre("Comédia Romântica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção Científica", true))
            )
        )
    }

    private fun sorted(expectedCategories: Iterable<CategoryID>) =
        expectedCategories.sortedBy { it.value }.toMutableList()
}
