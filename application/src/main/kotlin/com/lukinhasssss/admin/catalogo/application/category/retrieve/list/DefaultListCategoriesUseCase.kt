package com.lukinhasssss.admin.catalogo.application.category.retrieve.list

import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategorySearchQuery
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination

class DefaultListCategoriesUseCase(
    private val categoryGateway: CategoryGateway
) : ListCategoriesUseCase() {

    override fun execute(anIn: CategorySearchQuery): Pagination<CategoryListOutput> {
        return categoryGateway.findAll(aQuery = anIn).map { CategoryListOutput.from(it) }
    }
}
