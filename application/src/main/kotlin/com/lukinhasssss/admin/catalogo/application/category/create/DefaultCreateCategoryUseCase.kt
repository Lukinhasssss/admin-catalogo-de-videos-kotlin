package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.validation.handler.ThrowsValidationHandler

class DefaultCreateCategoryUseCase(
    private val categoryGateway: CategoryGateway
) : CreateCategoryUseCase() {

    override fun execute(aCommand: CreateCategoryCommand): CreateCategoryOutput {
        with(aCommand) {
            val aCategory = Category.newCategory(
                aName = name,
                aDescription = description,
                isActive = isActive
            ).apply { validate(ThrowsValidationHandler()) }

            return CreateCategoryOutput.from(categoryGateway.create(aCategory = aCategory))
        }
    }
}
