package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

interface CategoryGateway {

    fun create(aCategory: Category): Category

    fun findById(anID: CategoryID): Category?

    fun findAll(aQuery: SearchQuery): Pagination<Category>

    fun update(aCategory: Category): Category

    fun deleteById(anID: CategoryID)

    fun existsByIds(ids: Iterable<CategoryID>): List<CategoryID>
}
