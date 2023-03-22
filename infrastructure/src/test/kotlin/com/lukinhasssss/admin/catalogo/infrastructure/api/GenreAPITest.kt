package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ApiTest
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreOutput
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.delete.DeleteGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GenreOutput
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.GenreListOutput
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.ListGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreOutput
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreUseCase
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@ControllerTest(controllers = [GenreAPI::class])
class GenreAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var createGenreUseCase: CreateGenreUseCase

    @MockkBean
    private lateinit var getGenreByIdUseCase: GetGenreByIdUseCase

    @MockkBean
    private lateinit var updateGenreUseCase: UpdateGenreUseCase

    @MockkBean
    private lateinit var deleteGenreUseCase: DeleteGenreUseCase

    @MockkBean
    private lateinit var listGenreUseCase: ListGenreUseCase

    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val expectedId = "123"
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf("123", "456")

        val aCommand = CreateGenreRequest(expectedName, expectedIsActive, expectedCategories)

        every { createGenreUseCase.execute(any()) } returns CreateGenreOutput.from(expectedId)

        // when
        val aResponse = mvc.post(urlTemplate = "/genres") {
            with(ApiTest.GENRES_JWT)
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isCreated() }

            header {
                string(name = "Location", value = "/genres/$expectedId")
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId))
        }

        verify {
            createGenreUseCase.execute(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive)
                    assertEquals(expectedCategories, it.categories)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() {
        // given
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = listOf("123", "456")
        val expectedErrorMessage = "'name' should not be empty"

        val aCommand = CreateGenreRequest(expectedName, expectedIsActive, expectedCategories)

        every {
            createGenreUseCase.execute(any())
        } throws NotificationException("Error", Notification.create(Error(expectedErrorMessage)))

        // when
        val aResponse = mvc.post(urlTemplate = "/genres") {
            with(ApiTest.GENRES_JWT)
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isUnprocessableEntity() }

            header {
                doesNotExist("Location")
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.errors.size()", equalTo(1))
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
        }

        verify {
            createGenreUseCase.execute(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive)
                    assertEquals(expectedCategories, it.categories)
                }
            )
        }
    }

    @Test
    fun givenAValidId_whenCallsGetGenreByIdGenre_shouldReturnGenre() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf("123", "456")

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)
            .addCategories(expectedCategories.map { CategoryID.from(it) })

        val expectedId = aGenre.id.value

        every { getGenreByIdUseCase.execute(any()) } returns GenreOutput.from(aGenre)

        // when
        val aResponse = mvc.get(urlTemplate = "/genres/$expectedId") {
            with(ApiTest.GENRES_JWT)
        }

        // then
        aResponse.andExpect {
            status { isOk() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId))
            jsonPath("$.name", equalTo(expectedName))
            jsonPath("$.is_active", equalTo(expectedIsActive))
            jsonPath("$.categories_id", equalTo(expectedCategories))
            jsonPath("$.created_at", equalTo(aGenre.createdAt.toString()))
            jsonPath("$.updated_at", equalTo(aGenre.updatedAt.toString()))
            jsonPath("$.deleted_at", equalTo(aGenre.deletedAt.toString()))
        }

        verify { getGenreByIdUseCase.execute(expectedId) }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetGenreByIdGenre_shouldReturnNotFound() {
        // given
        val expectedId = GenreID.from(IdUtils.uuid())
        val expectedErrorMessage = "Genre with ID ${expectedId.value} was not found"

        every { getGenreByIdUseCase.execute(any()) } throws NotFoundException.with(expectedId, Genre::class)

        // when
        val aResponse = mvc.get(urlTemplate = "/genres/${expectedId.value}") {
            with(ApiTest.GENRES_JWT)
        }

        // then
        aResponse.andExpect {
            status { isNotFound() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.message", equalTo(expectedErrorMessage))
        }

        verify { getGenreByIdUseCase.execute(expectedId.value) }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf("123", "456")

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)

        val expectedId = aGenre.id.value

        val aCommand = UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories)

        every { updateGenreUseCase.execute(any()) } returns UpdateGenreOutput.from(aGenre)

        // when
        val aResponse = mvc.put(urlTemplate = "/genres/$expectedId") {
            with(ApiTest.GENRES_JWT)
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isOk() }

            header { string(name = "Content-Type", value = APPLICATION_JSON_VALUE) }

            jsonPath("$.id", equalTo(expectedId))
        }

        verify {
            updateGenreUseCase.execute(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive)
                    assertEquals(expectedCategories, it.categories)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() {
        // given
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = listOf("123", "456")
        val expectedErrorMessage = "'name' should not be empty"

        val aGenre = Genre.newGenre("Ação", expectedIsActive)

        val expectedId = aGenre.id.value

        val aCommand = UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories)

        every {
            updateGenreUseCase.execute(any())
        } throws NotificationException("Error", Notification.create(Error(expectedErrorMessage)))

        // when
        val aResponse = mvc.put(urlTemplate = "/genres/$expectedId") {
            with(ApiTest.GENRES_JWT)
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isUnprocessableEntity() }

            header { string(name = "Content-Type", value = APPLICATION_JSON_VALUE) }

            jsonPath("$.errors.size()", equalTo(1))
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
        }

        verify {
            updateGenreUseCase.execute(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive)
                    assertEquals(expectedCategories, it.categories)
                }
            )
        }
    }

    @Test
    fun givenAValidId_whenCallsDeleteGenre_shouldBeOK() {
        // given
        val expectedId = "123"

        every { deleteGenreUseCase.execute(any()) } returns Unit

        // when
        val aResponse = mvc.delete(urlTemplate = "/genres/$expectedId") {
            with(ApiTest.GENRES_JWT)
        }.andDo { print() }

        // then
        aResponse.andExpect { status { isNoContent() } }

        verify { deleteGenreUseCase.execute(expectedId) }
    }

    @Test
    fun givenValidParams_whenCallsListGenres_shouldReturnGenres() {
        // given
        val aGenre = Genre.newGenre("Ação", false)

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "ac"
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedItemsCount = 1
        val expectedTotal = 1
        val expectedItems = listOf(GenreListOutput.from(aGenre))

        every {
            listGenreUseCase.execute(any())
        } returns Pagination(expectedPage, expectedPerPage, expectedTotal.toLong(), expectedItems)

        // when
        val aResponse = mvc.get(urlTemplate = "/genres") {
            with(ApiTest.GENRES_JWT)
            param("page", expectedPage.toString())
            param("perPage", expectedPerPage.toString())
            param("sort", expectedSort)
            param("dir", expectedDirection)
            param("search", expectedTerms)
            accept(APPLICATION_JSON)
        }

        // then
        aResponse.andExpect {
            status { isOk() }
            jsonPath("$.current_page", equalTo(expectedPage))
            jsonPath("$.per_page", equalTo(expectedPerPage))
            jsonPath("$.total", equalTo(expectedTotal))
            jsonPath("$.items.size()", equalTo(expectedItemsCount))
            jsonPath("$.items[0].id", equalTo(aGenre.id.value))
            jsonPath("$.items[0].name", equalTo(aGenre.name))
            jsonPath("$.items[0].is_active", equalTo(aGenre.active))
            jsonPath("$.items[0].created_at", equalTo(aGenre.createdAt.toString()))
            jsonPath("$.items[0].deleted_at", equalTo(aGenre.deletedAt.toString()))
        }

        verify {
            listGenreUseCase.execute(
                withArg {
                    assertEquals(expectedPage, it.page)
                    assertEquals(expectedPerPage, it.perPage)
                    assertEquals(expectedDirection, it.direction)
                    assertEquals(expectedSort, it.sort)
                    assertEquals(expectedTerms, it.terms)
                }
            )
        }
    }
}
