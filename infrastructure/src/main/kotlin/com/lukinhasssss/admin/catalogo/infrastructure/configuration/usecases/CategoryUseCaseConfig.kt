package com.lukinhasssss.admin.catalogo.infrastructure.configuration.usecases

import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase
import com.lukinhasssss.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryUseCase
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CategoryUseCaseConfig(
    private val categoryGateway: CategoryGateway
) {

    @Bean
    fun createCategoryUseCase(): CreateCategoryUseCase {
        return DefaultCreateCategoryUseCase(categoryGateway)
    }

    @Bean
    fun updateCategoryUseCase(): UpdateCategoryUseCase {
        return DefaultUpdateCategoryUseCase(categoryGateway)
    }

    @Bean
    fun getCategoryByIdUseCase(): GetCategoryByIdUseCase {
        return DefaultGetCategoryByIdUseCase(categoryGateway)
    }

    @Bean
    fun listCategoriesUseCase(): ListCategoriesUseCase {
        return DefaultListCategoriesUseCase(categoryGateway)
    }
}
