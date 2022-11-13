package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.CategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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

    @MockBean
    private lateinit var getCategoryByIdUseCase: GetCategoryByIdUseCase

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

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        // given
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val expectedId = aCategory.id.value

        // when
        whenever(getCategoryByIdUseCase.execute(any())).thenReturn(CategoryOutput.from(aCategory))

        val request = get("/categories/$expectedId")

        val response = mvc.perform(request).andDo(print())

        // then
        with(aCategory) {
            response.andExpect(status().isOk)
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(createdAt.toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(updatedAt.toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(deletedAt)))
        }

        verify(getCategoryByIdUseCase, times(1)).execute(expectedId)
    }

    @Test
    fun givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        // given
        val expectedId = CategoryID.from("not-found")
        val expectedErrorMessage = "Category with ID ${expectedId.value} was not found"

        // when
        whenever(getCategoryByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(expectedId, Category::class))

        val request = get("/categories/$expectedId")

        val response = mvc.perform(request).andDo(print())

        // then
        response.andExpect(status().isNotFound)
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
    }
}
