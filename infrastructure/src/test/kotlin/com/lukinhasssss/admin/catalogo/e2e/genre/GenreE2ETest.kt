package com.lukinhasssss.admin.catalogo.e2e.genre

import com.lukinhasssss.admin.catalogo.E2ETest
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
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
class GenreE2ETest {

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    companion object {
        @Container
        val POSTGRESQL_CONTAINER: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:alpine")
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

    private fun givenACategory(aName: String, aDescription: String?, isActive: Boolean): CategoryID {
        val requestBody = CreateCategoryRequest(aName, aDescription, isActive)

        val actualId = Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            post("/api/categories")
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract {
            // header("Location").replace("/categories/", "") -> Essa é outra forma de recuperar o id
            jsonPath().get<String>("id")
        }

        return CategoryID.from(actualId)
    }

    private fun givenAGenre(aName: String, isActive: Boolean, categories: List<CategoryID>): GenreID {
        val aRequestBody = CreateGenreRequest(aName, isActive, mapTo(categories, CategoryID::value))

        val actualId = Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(aRequestBody))
        } When {
            post("/api/genres")
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract { jsonPath().get<String>("id") }

        return GenreID.from(actualId)
    }

    private fun <A, D> mapTo(actual: List<A>, mapper: (A) -> D): List<D> = actual.stream().map(mapper).toList()

    private fun sorted(expectedCategories: Iterable<CategoryID>) = expectedCategories.sortedBy { it.value }
}
