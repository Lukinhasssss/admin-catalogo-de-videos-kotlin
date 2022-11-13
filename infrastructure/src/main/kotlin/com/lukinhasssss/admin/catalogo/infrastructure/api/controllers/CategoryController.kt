package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.api.CategoryAPI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController : CategoryAPI {
    override fun createCategory(): ResponseEntity<*> {
        TODO("Not yet implemented")
    }

    override fun listCategories(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String
    ): Pagination<*> {
        TODO("Not yet implemented")
    }
}
