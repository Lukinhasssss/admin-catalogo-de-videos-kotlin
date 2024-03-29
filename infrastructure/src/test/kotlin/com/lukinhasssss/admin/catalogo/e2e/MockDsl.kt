package com.lukinhasssss.admin.catalogo.e2e

import com.lukinhasssss.admin.catalogo.KeycloakTestContainers
import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CastMemberResponse
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.UpdateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

interface MockDsl : KeycloakTestContainers {

    companion object {
        const val BASE_URL = "http://localhost:8081/api"
    }

    /* START OF CAST MEMBER MOCKS */

    fun deleteACastMember(anId: CastMemberID) = delete("/cast_members", anId)

    fun givenACastMember(aName: String, aType: CastMemberType): CastMemberID {
        val requestBody = CreateCastMemberRequest(aName, aType)

        val actualId = given("/cast_members", requestBody)

        return CastMemberID.from(actualId)
    }

    fun givenACastMemberResponse(aName: String, aType: CastMemberType): Response {
        val requestBody = CreateCastMemberRequest(aName, aType)

        return givenResult("/cast_members", requestBody)
    }

    fun listCastMembers(page: Int, perPage: Int) =
        listCastMembers(page = page, perPage = perPage, search = "", sort = "", direction = "")

    fun listCastMembers(page: Int, perPage: Int, search: String) =
        listCastMembers(page = page, perPage = perPage, search = search, sort = "", direction = "")

    fun listCastMembers(
        page: Int,
        perPage: Int,
        search: String,
        sort: String,
        direction: String
    ) = list("/cast_members", page, perPage, search, sort, direction)

    fun retrieveACastMember(anId: CastMemberID) = retrieve("/cast_members", anId, CastMemberResponse::class)

    fun retrieveACastMemberResponse(anId: CastMemberID) = retrieveResult("/cast_members", anId)

    fun updateACastMember(anId: CastMemberID, aName: String, aType: CastMemberType): Response =
        update("/cast_members", anId, UpdateCastMemberRequest(aName, aType))

    /* END OF CAST MEMBER MOCKS */

    /* START OF CATEGORY MOCKS */

    fun deleteACategory(anId: CategoryID) = delete("/categories", anId)

    fun givenACategory(aName: String, aDescription: String?, isActive: Boolean): CategoryID {
        val requestBody = CreateCategoryRequest(aName, aDescription, isActive)

        val actualId = given(resource = "/categories", requestBody)

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

    fun retrieveACategory(anId: CategoryID) = retrieve("/categories", anId, CategoryResponse::class)

    fun retrieveACategoryResponse(anId: CategoryID) = retrieveResult("/categories", anId)

    fun updateACategory(anId: CategoryID, requestBody: UpdateCategoryRequest) = update("/categories", anId, requestBody)

    /* END OF CATEGORY MOCKS */

    /* START OF GENRE MOCKS */

    fun deleteAGenre(anId: GenreID) = delete("/genres", anId)

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

    fun retrieveAGenre(anId: GenreID) = retrieve("/genres", anId, GenreResponse::class)

    fun retrieveAGenreResponse(anId: GenreID) = retrieveResult("/genres", anId)

    fun updateAGenre(anId: GenreID, requestBody: UpdateGenreRequest) = update("/genres", anId, requestBody)

    /* END OF GENRE MOCKS */

    /* START OF AUXILIAR FUNCTIONS */

    private fun delete(resource: String, anId: Identifier): Response {
        return Given {
            header(AUTHORIZATION, getAccessToken())
        } When { delete("$BASE_URL$resource/${anId.value}") }
    }

    private fun given(resource: String, requestBody: Any): String {
        val actualId = Given {
            header(AUTHORIZATION, getAccessToken())
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            post("$BASE_URL$resource")
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract { jsonPath().get<String>("id") }

        return actualId
    }

    private fun givenResult(resource: String, requestBody: Any): Response {
        return Given {
            header(AUTHORIZATION, getAccessToken())
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When { post("$BASE_URL$resource") }
    }

    private fun list(
        resource: String,
        page: Int,
        perPage: Int,
        search: String,
        sort: String,
        direction: String
    ): Response {
        return Given {
            header(AUTHORIZATION, getAccessToken())
            param("page", page)
            param("perPage", perPage)
            param("search", search)
            param("sort", sort)
            param("dir", direction)
        } When {
            get("$BASE_URL$resource")
        }
    }

    private fun <T : Any> retrieve(resource: String, anId: Identifier, kclass: KClass<T>): T {
        val json = Given {
            header(AUTHORIZATION, getAccessToken())
        } When {
            get("$BASE_URL$resource/${anId.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract { body().asString() }

        return Json.readValue(json, kclass.java)
    }

    private fun retrieveResult(resource: String, anId: Identifier): Response =
        Given {
            header("Authorization", getAccessToken())
        } When {
            get("$BASE_URL$resource/${anId.value}")
        }

    private fun update(resource: String, anId: Identifier, requestBody: Any): Response {
        val response = Given {
            header(AUTHORIZATION, getAccessToken())
            contentType(ContentType.JSON)
            body(Json.writeValueAsString(requestBody))
        } When {
            put("$BASE_URL$resource/${anId.value}")
        }

        return response
    }

    fun <A, D> mapTo(actual: List<A>, mapper: (A) -> D): List<D> = actual.stream().map(mapper).toList()

    /* END OF AUXILIAR FUNCTIONS */
}
