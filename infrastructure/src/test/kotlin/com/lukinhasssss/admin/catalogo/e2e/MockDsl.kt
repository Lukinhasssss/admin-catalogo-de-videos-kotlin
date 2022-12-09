package com.lukinhasssss.admin.catalogo.e2e

import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpStatus

interface MockDsl {

    fun givenACategory(aName: String, aDescription: String?, isActive: Boolean): CategoryID {
        val requestBody = CreateCategoryRequest(aName, aDescription, isActive)

        val actualId = given(url = "/api/categories", requestBody)

        return CategoryID.from(actualId)
    }

    fun givenAGenre(aName: String, isActive: Boolean, categories: List<CategoryID>): GenreID {
        val requestBody = CreateGenreRequest(aName, isActive, mapTo(categories, CategoryID::value))

        val actualId = given("/api/genres", requestBody)

        return GenreID.from(actualId)
    }

    private fun given(url: String, requestBody: Any): String {
        val actualId = Given {
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            post(url)
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract { jsonPath().get<String>("id") }

        return actualId
    }

    private fun <A, D> mapTo(actual: List<A>, mapper: (A) -> D): List<D> = actual.stream().map(mapper).toList()
}
