package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberCommand
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.delete.DeleteCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.GetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.ListCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.update.UpdateCastMemberCommand
import com.lukinhasssss.admin.catalogo.application.castMember.update.UpdateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.api.CastMemberAPI
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CastMemberListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CastMemberResponse
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.UpdateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.presenters.toCastMemberListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.presenters.toCastMemberResponse
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CastMemberController(
    private val createCastMemberUseCase: CreateCastMemberUseCase,
    private val getCastMemberByIdUseCase: GetCastMemberByIdUseCase,
    private val listCastMemberUseCase: ListCastMemberUseCase,
    private val updateCastMemberUseCase: UpdateCastMemberUseCase,
    private val deleteCastMemberUseCase: DeleteCastMemberUseCase
) : CastMemberAPI {

    override fun create(request: CreateCastMemberRequest): ResponseEntity<Any> = with(request) {
        Logger.info(message = "Iniciando processo de criação de membro de elenco...", payload = request)

        val aCommand = CreateCastMemberCommand.with(name, type)

        val output = createCastMemberUseCase.execute(aCommand)

        Logger.info(message = "Finalizado processo de criação de membro de elenco!")
        ResponseEntity.created(URI.create("/cast_members/${output.id}")).body(output)
    }

    override fun getById(id: String): CastMemberResponse {
        Logger.info(message = "Iniciando processo de busca de membro de elenco...")

        return getCastMemberByIdUseCase.execute(id).toCastMemberResponse().also {
            Logger.info(message = "Finalizado processo de busca de membro de elenco!")
        }
    }

    override fun list(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String
    ): Pagination<CastMemberListResponse> {
        Logger.info(message = "Iniciando processo de listagem de membros de elenco...")

        val aQuery = SearchQuery(page, perPage, search, sort, direction)

        return listCastMemberUseCase.execute(aQuery).map { it.toCastMemberListResponse() }.also {
            Logger.info(message = "Finalizado processo de listagem de membros de elenco!")
        }
    }

    override fun updateById(
        id: String,
        request: UpdateCastMemberRequest
    ): ResponseEntity<Any> = with(request) {
        Logger.info(message = "Iniciando processo de atualização de membro de elenco...")

        val aCommand = UpdateCastMemberCommand.with(anId = id, aName = name, aType = type)

        val output = updateCastMemberUseCase.execute(aCommand)

        Logger.info(message = "Finalizado processo de atualização de membro de elenco!")
        return ResponseEntity.ok(output)
    }

    override fun deleteById(id: String) {
        Logger.info(message = "Iniciando processo de deleção de membro de elenco...")

        deleteCastMemberUseCase.execute(id).also {
            Logger.info(message = "Finalizado processo de deleção de membro de elenco!")
        }
    }
}
