package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreOutput
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
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
import org.springframework.test.web.servlet.post

@ControllerTest(controllers = [GenreAPI::class])
class GenreAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var createGenreUseCase: CreateGenreUseCase

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
    fun givenAnInvalidCommand_whenCallsCreateGenre_shouldReturnNotification() {
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
}
