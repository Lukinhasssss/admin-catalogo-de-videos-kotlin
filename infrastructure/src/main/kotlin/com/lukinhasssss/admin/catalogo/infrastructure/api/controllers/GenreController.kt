package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreCommand
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.delete.DeleteGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreCommand
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreUseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.api.GenreAPI
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.presenters.toGenreResponse
import org.hibernate.engine.transaction.internal.jta.JtaStatusHelper.isActive
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class GenreController(
    private val createGenreUseCase: CreateGenreUseCase,
    private val getGenreByIdUseCase: GetGenreByIdUseCase,
    private val updateGenreUseCase: UpdateGenreUseCase,
    private val deleteGenreUseCase: DeleteGenreUseCase
) : GenreAPI {

    override fun create(request: CreateGenreRequest): ResponseEntity<Any> = with(request) {
        val aCommand = CreateGenreCommand.with(name, isActive(), categories)

        val output = createGenreUseCase.execute(aCommand)

        ResponseEntity.created(URI.create("/genres/${output.id}")).body(output)
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

    override fun updateById(id: String, request: UpdateGenreRequest): ResponseEntity<Any> = with(request) {
        val aCommand = UpdateGenreCommand.with(id, name, isActive(), categories)

        val output = updateGenreUseCase.execute(aCommand)

        return ResponseEntity.ok(output)
    }

    override fun deleteById(id: String) = deleteGenreUseCase.execute(id)
}
