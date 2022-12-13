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
        val aCommand = CreateCastMemberCommand.with(name, type)

        val output = createCastMemberUseCase.execute(aCommand)

        ResponseEntity.created(URI.create("/cast_members/${output.id}")).body(output)
    }

    override fun getById(id: String): CastMemberResponse =
        getCastMemberByIdUseCase.execute(id).toCastMemberResponse()

    override fun list(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String,
    ): Pagination<CastMemberListResponse> {
        val aQuery = SearchQuery(page, perPage, search, sort, direction)

        return listCastMemberUseCase.execute(aQuery).map { it.toCastMemberListResponse() }
    }

    override fun updateById(id: String, request: UpdateCastMemberRequest): ResponseEntity<Any> = with(request) {
        val aCommand = UpdateCastMemberCommand.with(anId = id, aName = name, aType = type)

        return ResponseEntity.ok(updateCastMemberUseCase.execute(aCommand))
    }

    override fun deleteById(id: String) = deleteCastMemberUseCase.execute(id)
}
