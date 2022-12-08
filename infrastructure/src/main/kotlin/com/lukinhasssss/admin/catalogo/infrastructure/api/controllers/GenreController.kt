package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreCommand
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.api.GenreAPI
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.presenters.toGenreResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class GenreController(
    val createGenreUseCase: CreateGenreUseCase,
    val getGenreByIdUseCase: GetGenreByIdUseCase
) : GenreAPI {

    override fun create(request: CreateGenreRequest): ResponseEntity<Any> = with(request) {
        val aCommand = CreateGenreCommand.with(name, isActive(), categories)

        val output = createGenreUseCase.execute(aCommand)

        return ResponseEntity.created(URI.create("/genres/${output.id}")).body(output)
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

    override fun getById(id: String) = getGenreByIdUseCase.execute(id).toGenreResponse()

    override fun updateById(id: String, request: UpdateGenreRequest): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: String) {
        TODO("Not yet implemented")
    }
}
