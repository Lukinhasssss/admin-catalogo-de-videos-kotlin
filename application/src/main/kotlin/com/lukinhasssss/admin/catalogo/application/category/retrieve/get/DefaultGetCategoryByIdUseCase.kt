package com.lukinhasssss.admin.catalogo.application.category.retrieve.get

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.Error

class DefaultGetCategoryByIdUseCase(
    private val categoryGateway: CategoryGateway
) : GetCategoryByIdUseCase() {

    override fun execute(anIn: String): CategoryOutput {
        val anCategoryID = CategoryID.from(anIn)

        return categoryGateway.findById(anCategoryID)?.map()
            ?: throw notFound(anCategoryID)
    }

    private fun Category.map() =
        CategoryOutput(id, name, description, isActive, createdAt, updatedAt, deletedAt)

    private fun notFound(anCategoryID: CategoryID) =
        DomainException.with(Error("Category with ID ${anCategoryID.value} was not found"))
}
