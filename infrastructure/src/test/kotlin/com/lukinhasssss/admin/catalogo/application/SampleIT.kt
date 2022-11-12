package com.lukinhasssss.admin.catalogo.application

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class SampleIT {

    @Autowired
    private lateinit var usecase: CreateCategoryUseCase

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun test() {
        assertNotNull(usecase)
        assertNotNull(categoryRepository)
    }
}
