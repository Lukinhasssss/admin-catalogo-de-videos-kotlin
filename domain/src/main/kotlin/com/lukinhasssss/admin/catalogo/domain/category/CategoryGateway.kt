package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination

interface CategoryGateway {

    fun create(aCategory: Category): Category

    fun findById(anID: CategoryID): Category?

    fun findAll(aQuery: CategorySearchQuery): Pagination<Category>

    fun update(aCategory: Category): Category

    fun deleteById(anID: CategoryID)
}
