package com.lukinhasssss.admin.catalogo.application.category.retrieve.list

import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

class DefaultListCategoriesUseCase(
    private val categoryGateway: CategoryGateway
) : ListCategoriesUseCase() {

    override fun execute(anIn: SearchQuery): Pagination<CategoryListOutput> {
        return categoryGateway.findAll(aQuery = anIn).map { CategoryListOutput.from(it) }
    }
}
