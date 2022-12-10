package com.lukinhasssss.admin.catalogo.e2e

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreResponse
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

interface MockDsl {

    /* START OF CATEGORY MOCKS */

    fun deleteACategory(anId: Identifier) = delete("/categories", anId)

    fun givenACategory(aName: String, aDescription: String?, isActive: Boolean): CategoryID {
        val requestBody = CreateCategoryRequest(aName, aDescription, isActive)

        val actualId = given(url = "/categories", requestBody)

        return CategoryID.from(actualId)
    }

    fun listCategories(page: Int, perPage: Int) =
        listCategories(page = page, perPage = perPage, search = "", sort = "", direction = "")

    fun listCategories(page: Int, perPage: Int, search: String) =
        listCategories(page = page, perPage = perPage, search = search, sort = "", direction = "")

    fun listCategories(
        page: Int,
        perPage: Int,
        search: String,
        sort: String,
        direction: String
    ) = list("/categories", page, perPage, search, sort, direction)

    fun retrieveACategory(anId: Identifier) = retrieve("/categories", anId, CategoryResponse::class)

    fun updateACategory(anId: Identifier, requestBody: UpdateCategoryRequest) = update("/categories", anId, requestBody)

    /* END OF CATEGORY MOCKS */

    /* START OF GENRE MOCKS */

    fun givenAGenre(aName: String, isActive: Boolean, categories: List<CategoryID>): GenreID {
        val requestBody = CreateGenreRequest(aName, isActive, mapTo(categories, CategoryID::value))

        val actualId = given("/genres", requestBody)

        return GenreID.from(actualId)
    }

    fun listGenres(page: Int, perPage: Int) =
        listGenres(page = page, perPage = perPage, search = "", sort = "", direction = "")

    fun listGenres(page: Int, perPage: Int, search: String) =
        listGenres(page = page, perPage = perPage, search = search, sort = "", direction = "")

    fun listGenres(
        page: Int,
        perPage: Int,
        search: String,
        sort: String,
        direction: String
    ) = list("/genres", page, perPage, search, sort, direction)

    fun retrieveAGenre(anId: Identifier) = retrieve("/genres", anId, GenreResponse::class)

    /* END OF GENRE MOCKS */

    /* START OF AUXILIAR FUNCTIONS */

    private fun delete(url: String, anId: Identifier): Response {
        return When { delete("/api$url/${anId.value}") }
    }

    private fun given(url: String, requestBody: Any): String {
        val actualId = Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            post("/api$url")
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract { jsonPath().get<String>("id") }

        return actualId
    }

    private fun list(
        url: String,
        page: Int,
        perPage: Int,
        search: String,
        sort: String,
        direction: String
    ): Response {
        return Given {
            param("page", page)
            param("perPage", perPage)
            param("search", search)
            param("sort", sort)
            param("dir", direction)
        } When { get("/api$url") }
    }

    private fun <T : Any> retrieve(url: String, anId: Identifier, kclass: KClass<T>): T {
        val json = When {
            get("/api$url/${anId.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract { body().asString() }

        return Json.readValue(json, kclass.java)
    }

    private fun update(url: String, anId: Identifier, requestBody: Any): Response {
        val response = Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            put("/api$url/${anId.value}")
        }

        return response
    }

    private fun <A, D> mapTo(actual: List<A>, mapper: (A) -> D): List<D> = actual.stream().map(mapper).toList()

    /* END OF AUXILIAR FUNCTIONS */
}
