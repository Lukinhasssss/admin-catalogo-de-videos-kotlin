package com.lukinhasssss.admin.catalogo.application.category.retrieve.list

import com.lukinhasssss.admin.catalogo.application.UseCase
import com.lukinhasssss.admin.catalogo.domain.category.CategorySearchQuery
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination

abstract class ListCategoriesUseCase : UseCase<CategorySearchQuery, Pagination<CategoryListOutput>>()
