package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberCommand
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.GetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.infrastructure.api.CastMemberAPI
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CastMemberResponse
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.presenters.toCastMemberResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CastMemberController(
    private val createCastMemberUseCase: CreateCastMemberUseCase,
    private val getCastMemberByIdUseCase: GetCastMemberByIdUseCase
) : CastMemberAPI {

    override fun create(request: CreateCastMemberRequest): ResponseEntity<Any> = with(request) {
        val aCommand = CreateCastMemberCommand.with(name, type)

        val output = createCastMemberUseCase.execute(aCommand)

        ResponseEntity.created(URI.create("/cast_members/${output.id}")).body(output)
    }

    override fun getById(id: String): CastMemberResponse =
        getCastMemberByIdUseCase.execute(id).toCastMemberResponse()
}
