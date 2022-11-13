package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.MockBean

@ControllerTest(controllers = [CategoryAPI::class])
class CategoryAPITest {

    @MockBean
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    @Test
    fun test() {
    }
}
