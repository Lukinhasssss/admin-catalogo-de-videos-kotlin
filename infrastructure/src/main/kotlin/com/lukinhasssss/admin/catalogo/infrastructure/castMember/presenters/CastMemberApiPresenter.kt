package com.lukinhasssss.admin.catalogo.infrastructure.castMember.presenters

import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.CastMemberOutput
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CastMemberResponse

fun CastMemberOutput.toCastMemberResponse() =
    CastMemberResponse(
        id = id,
        name = name,
        type = type.name,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
