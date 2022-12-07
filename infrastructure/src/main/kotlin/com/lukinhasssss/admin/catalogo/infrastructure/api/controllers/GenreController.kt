package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.api.GenreAPI
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GenreController : GenreAPI {

    override fun create(request: CreateGenreRequest): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }

    override fun list(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String,
    ): Pagination<GenreListResponse> {
        TODO("Not yet implemented")
    }

    override fun getById(id: String): GenreResponse {
        TODO("Not yet implemented")
    }

    override fun updateById(id: String, request: UpdateGenreRequest): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: String) {
        TODO("Not yet implemented")
    }
}
