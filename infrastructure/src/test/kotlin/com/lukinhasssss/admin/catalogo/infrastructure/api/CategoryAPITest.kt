package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import io.vavr.kotlin.left
import io.vavr.kotlin.right
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ControllerTest(controllers = [CategoryAPI::class])
class CategoryAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        // given
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val anInput = CreateCategoryRequest(
            name = expectedName,
            description = expectedDescription,
            active = expectedIsActive
        )

        whenever(createCategoryUseCase.execute(any()))
            .thenReturn(right(CreateCategoryOutput.from("123")))

        // when
        val request = post("/categories")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        val response = mvc.perform(request).andDo(print())

        // then
        response.andExpect(status().isCreated)
            .andExpect(header().string("Location", "/categories/123"))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo("123")))

        verify(createCategoryUseCase, times(1)).execute(
            argThat {
                expectedName == name && expectedDescription == description && expectedIsActive == isActive
            }
        )
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnNotification() {
        // given
        val expectedName = "      "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be empty"

        val anInput = CreateCategoryRequest(
            name = expectedName,
            description = expectedDescription,
            active = expectedIsActive
        )

        whenever(createCategoryUseCase.execute(any()))
            .thenReturn(left(Notification.create(Error(expectedMessage))))

        // when
        val request = post("/categories")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        val response = mvc.perform(request).andDo(print())

        // then
        response.andExpect(status().isUnprocessableEntity)
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors.size()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)))

        verify(createCategoryUseCase, times(1)).execute(
            argThat {
                expectedName == name && expectedDescription == description && expectedIsActive == isActive
            }
        )
    }

    @Test
    fun givenAnInvalidCommand_whenCallsCreateCategory_shouldReturnDomainException() {
        // given
        val expectedName = "      "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be empty"

        val anInput = CreateCategoryRequest(
            name = expectedName,
            description = expectedDescription,
            active = expectedIsActive
        )

        whenever(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(Error(expectedMessage)))

        // when
        val request = post("/categories")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        val response = mvc.perform(request).andDo(print())

        // then
        response.andExpect(status().isUnprocessableEntity)
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedMessage)))
            .andExpect(jsonPath("$.errors.size()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)))

        verify(createCategoryUseCase, times(1)).execute(
            argThat {
                expectedName == name && expectedDescription == description && expectedIsActive == isActive
            }
        )
    }
}
