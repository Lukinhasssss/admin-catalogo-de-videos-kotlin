package com.lukinhasssss.admin.catalogo.e2e.genre

import com.lukinhasssss.admin.catalogo.E2ETest
import com.lukinhasssss.admin.catalogo.KeycloakTestContainers
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.e2e.MockDsl
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import io.restassured.module.kotlin.extensions.Then
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
@Testcontainers
class GenreE2ETest : MockDsl, KeycloakTestContainers {

    @Autowired
    private lateinit var genreRepository: GenreRepository

    companion object {
        @Container
        val POSTGRESQL_CONTAINER: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:latest")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("adm_videos")

        /* Este metodo serve para customizar as properties do spring em runtime com base no que o container vai gerar */
        @JvmStatic
        @DynamicPropertySource
        fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("postgres.port") { POSTGRESQL_CONTAINER.getMappedPort(5432) }
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToCreateANewGenre() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()

        val actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories)

        val actualGenre = genreRepository.findById(actualId.value).get()

        with(actualGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val filmes = givenACategory("Filmes", null, true)

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes)

        val actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories)

        val actualGenre = genreRepository.findById(actualId.value).get()

        with(actualGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToNavigateThroughAllCategories() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val expectedStatusCode = HttpStatus.OK.value()

        givenAGenre("Ação", true, listOf())
        givenAGenre("Esportes", true, listOf())
        givenAGenre("Drama", true, listOf())

        listGenres(page = 0, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Ação"))
        }

        listGenres(page = 1, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(1))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Drama"))
        }

        listGenres(page = 2, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(2))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Esportes"))
        }

        listGenres(page = 3, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(3))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            // body("$", not(hasKey("items")))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleSearchBetweenAllGenres() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val expectedStatusCode = HttpStatus.OK.value()

        givenAGenre("Ação", true, listOf())
        givenAGenre("Esportes", true, listOf())
        givenAGenre("Drama", true, listOf())

        listGenres(page = 0, perPage = 1, search = "dra") Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(1))
            body("total", equalTo(1))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Drama"))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeToSortAllGenresByNameDesc() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val expectedStatusCode = HttpStatus.OK.value()

        givenAGenre("Ação", true, listOf())
        givenAGenre("Esportes", true, listOf())
        givenAGenre("Drama", true, listOf())

        listGenres(page = 0, perPage = 3, search = "", sort = "name", direction = "desc") Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(3))
            body("total", equalTo(3))
            body("items.size()", equalTo(3))
            body("items.get(0).name", equalTo("Esportes"))
            body("items.get(1).name", equalTo("Drama"))
            body("items.get(2).name", equalTo("Ação"))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val filmes = givenACategory("Filmes", null, true)

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes)

        val actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories)

        val actualGenre = retrieveAGenre(actualId)

        with(actualGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(categories.map { CategoryID.from(it) }))
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val expectedStatusCode = HttpStatus.NOT_FOUND.value()
        val expectedMessage = "Genre with ID 123 was not found"

        retrieveAGenreResponse(GenreID.from("123")) Then {
            statusCode(expectedStatusCode)
            body("message", equalTo(expectedMessage))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val filmes = givenACategory("Filmes", null, true)

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes)

        val actualId = givenAGenre("Action", expectedIsActive, expectedCategories)

        val requestBody = UpdateGenreRequest(expectedName, expectedIsActive, mapTo(expectedCategories, CategoryID::value))

        updateAGenre(actualId, requestBody) Then { statusCode(HttpStatus.OK.value()) }

        val actualGenre = genreRepository.findById(actualId.value).get()

        with(actualGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val filmes = givenACategory("Filmes", null, true)

        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf(filmes)

        val actualId = givenAGenre(expectedName, true, expectedCategories)

        val requestBody = UpdateGenreRequest(expectedName, expectedIsActive, mapTo(expectedCategories, CategoryID::value))

        updateAGenre(actualId, requestBody) Then { statusCode(HttpStatus.OK.value()) }

        val actualGenre = genreRepository.findById(actualId.value).get()

        with(actualGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val filmes = givenACategory("Filmes", null, true)

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes)

        val actualId = givenAGenre(expectedName, false, expectedCategories)

        val requestBody = UpdateGenreRequest(expectedName, expectedIsActive, mapTo(expectedCategories, CategoryID::value))

        updateAGenre(actualId, requestBody) Then { statusCode(HttpStatus.OK.value()) }

        val actualGenre = genreRepository.findById(actualId.value).get()

        with(actualGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(sorted(expectedCategories), sorted(getCategoryIDs()))
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        val filmes = givenACategory("Filmes", null, true)

        val actualId = givenAGenre("Ação", true, listOf(filmes))

        deleteAGenre(actualId) Then { statusCode(HttpStatus.NO_CONTENT.value()) }

        Assertions.assertFalse(genreRepository.existsById(actualId.value))
    }

    @Test
    fun asACatalogAdminIShouldNotSeeAnErrorByDeletingAGenreThatNotExists() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, genreRepository.count())

        givenAGenre("Ação", true, listOf())

        deleteAGenre(GenreID.from("123")) Then { statusCode(HttpStatus.NO_CONTENT.value()) }

        assertEquals(1, genreRepository.count())
    }

    private fun sorted(expectedCategories: Iterable<CategoryID>) = expectedCategories.sortedBy { it.value }
}
