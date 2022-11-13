package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import io.vavr.kotlin.right
import org.hamcrest.Matchers.equalTo
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
        response.andExpect {
            status().isCreated
            header().string("Location", "/categories/123")
            header().string("Content-Type", APPLICATION_JSON_VALUE)
            jsonPath("$.id", equalTo("123"))
        }

        verify(createCategoryUseCase, times(1)).execute(
            argThat {
                expectedName == name && expectedDescription == description && expectedIsActive == isActive
            }
        )
    }
}
