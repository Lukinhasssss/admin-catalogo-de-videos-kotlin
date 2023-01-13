package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreCommand
import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.delete.DeleteGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.ListGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreCommand
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreUseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.api.GenreAPI
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.lukinhasssss.admin.catalogo.infrastructure.genre.presenters.toGenreListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.presenters.toGenreResponse
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class GenreController(
    private val createGenreUseCase: CreateGenreUseCase,
    private val getGenreByIdUseCase: GetGenreByIdUseCase,
    private val updateGenreUseCase: UpdateGenreUseCase,
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val listGenreUseCase: ListGenreUseCase
) : GenreAPI {

    override fun create(request: CreateGenreRequest): ResponseEntity<Any> = with(request) {
        Logger.info(message = "Iniciando processo de criação de gênero...", payload = request)

        val aCommand = CreateGenreCommand.with(name, isActive(), categories)

        val output = createGenreUseCase.execute(aCommand)

        Logger.info(message = "Finalizado processo de criação de gênero!")
        ResponseEntity.created(URI.create("/genres/${output.id}")).body(output)
    }

    override fun list(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String,
    ): Pagination<GenreListResponse> {
        Logger.info(message = "Iniciando processo de listagem de gêneros...")

        val aQuery = SearchQuery(page, perPage, search, sort, direction)

        return listGenreUseCase.execute(aQuery).map { it.toGenreListResponse() }.also {
            Logger.info(message = "Finalizado processo de listagem de gêneros")
        }
    }

    override fun getById(id: String): GenreResponse {
        Logger.info(message = "Iniciando processo de busca de gênero...")

        return getGenreByIdUseCase.execute(id).toGenreResponse().also {
            Logger.info(message = "Finalizado processo de busca de gênero!")
        }
    }

    override fun updateById(id: String, request: UpdateGenreRequest): ResponseEntity<Any> = with(request) {
        Logger.info(message = "Iniciando processo de atualização de gênero...", payload = request)

        val aCommand = UpdateGenreCommand.with(id, name, isActive(), categories)

        val output = updateGenreUseCase.execute(aCommand)

        Logger.info(message = "Finalizado processo de atualização de gênero!", payload = request)
        return ResponseEntity.ok(output)
    }

    override fun deleteById(id: String) {
        Logger.info(message = "Iniciando processo de deleção de gênero...")

        deleteGenreUseCase.execute(id).also {
            Logger.info(message = "Finalizado processo de deleção de gênero!")
        }
    }
}
