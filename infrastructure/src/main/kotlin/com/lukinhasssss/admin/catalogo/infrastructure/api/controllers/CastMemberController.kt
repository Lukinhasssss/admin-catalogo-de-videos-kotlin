package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberCommand
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.infrastructure.api.CastMemberAPI
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CastMemberController(
    private val createCastMemberUseCase: CreateCastMemberUseCase
) : CastMemberAPI {

    override fun create(request: CreateCastMemberRequest): ResponseEntity<Any> = with(request) {
        val aCommand = CreateCastMemberCommand.with(name, type)

        val output = createCastMemberUseCase.execute(aCommand)

        ResponseEntity.created(URI.create("/cast_members/${output.id}")).body(output)
    }
}
