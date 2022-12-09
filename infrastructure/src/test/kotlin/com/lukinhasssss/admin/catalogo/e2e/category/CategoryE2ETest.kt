package com.lukinhasssss.admin.catalogo.e2e.category

import com.lukinhasssss.admin.catalogo.E2ETest
import com.lukinhasssss.admin.catalogo.e2e.MockDsl
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
class CategoryE2ETest : MockDsl {

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    companion object {
        @Container
        val POSTGRESQL_CONTAINER: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:alpine")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("adm_videos")

        /**
         * Este metodo serve para customizar as properties do spring em runtime com base no que o container vai gerar
         */
        @JvmStatic
        @DynamicPropertySource
        fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("postgres.port") { POSTGRESQL_CONTAINER.getMappedPort(5432) }
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val actualId = givenACategory(expectedName, expectedDescription, expectedIsActive)

        val actualCategory = categoryRepository.findById(actualId.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToNavigateToAllCategories() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedStatusCode = HttpStatus.OK.value()

        givenACategory("Filmes", null, true)
        givenACategory("Animes", null, true)
        givenACategory("Séries", null, true)

        listCategories(page = 0, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Animes"))
        }

        listCategories(page = 1, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(1))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Filmes"))
        }

        listCategories(page = 2, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(2))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Séries"))
        }

        listCategories(page = 3, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(3))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            // body("items.size()", equalTo(0))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleSearchBetweenAllCategories() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedStatusCode = HttpStatus.OK.value()

        givenACategory("Filmes", null, true)
        givenACategory("Animes", null, true)
        givenACategory("Séries", null, true)

        listCategories(page = 0, perPage = 1, search = "fil") Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(1))
            body("total", equalTo(1))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Filmes"))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeToSortAllCategoriesByDescriptionDesc() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedStatusCode = HttpStatus.OK.value()

        givenACategory("Filmes", "A", true)
        givenACategory("Animes", "B", true)
        givenACategory("Séries", "C", true)

        listCategories(page = 0, perPage = 3, search = "", sort = "description", direction = "desc") Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(3))
            body("total", equalTo(3))
            body("items.size()", equalTo(3))
            body("items.get(0).name", equalTo("Séries"))
            body("items.get(1).name", equalTo("Animes"))
            body("items.get(2).name", equalTo("Filmes"))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val actualId = givenACategory(expectedName, expectedDescription, expectedIsActive)

        val actualCategory = retrieveACategory(actualId.value)

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, active)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedMessage = "Category with ID 123 was not found"

        When {
            get("/api/categories/123")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", equalTo(expectedMessage))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val actualId = givenACategory("Movies", null, true)

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val requestBody = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            put("/api/categories/${actualId.value}")
        } Then { statusCode(HttpStatus.OK.value()) }

        val actualCategory = categoryRepository.findById(actualId.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val actualId = givenACategory(expectedName, expectedDescription, true)

        val requestBody = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            put("/api/categories/${actualId.value}")
        } Then { statusCode(HttpStatus.OK.value()) }

        val actualCategory = categoryRepository.findById(actualId.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val actualId = givenACategory(expectedName, expectedDescription, false)

        val requestBody = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            put("/api/categories/${actualId.value}")
        } Then { statusCode(HttpStatus.OK.value()) }

        val actualCategory = categoryRepository.findById(actualId.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, categoryRepository.count())

        val actualId = givenACategory("Filmes", null, true)

        When {
            delete("/api/categories/${actualId.value}")
        } Then {
            statusCode(HttpStatus.NO_CONTENT.value())
        }

        assertFalse(categoryRepository.existsById(actualId.value))
    }

    private fun listCategories(page: Int, perPage: Int): Response {
        return listCategories(page = page, perPage = perPage, search = "", sort = "", direction = "")
    }

    private fun listCategories(page: Int, perPage: Int, search: String): Response {
        return listCategories(page = page, perPage = perPage, search = search, sort = "", direction = "")
    }

    private fun listCategories(page: Int, perPage: Int, search: String, sort: String, direction: String): Response {
        return Given {
            param("page", page)
            param("perPage", perPage)
            param("search", search)
            param("sort", sort)
            param("dir", direction)
        } When { get("/api/categories") }
    }

    private fun retrieveACategory(anId: String): CategoryResponse {
        val json = When {
            get("/api/categories/$anId")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract { body().asString() }

        return Json.readValue(json, CategoryResponse::class.java)
    }
}
