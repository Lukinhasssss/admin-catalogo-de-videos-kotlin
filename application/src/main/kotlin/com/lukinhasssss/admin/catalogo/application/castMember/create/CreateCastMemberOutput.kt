package com.lukinhasssss.admin.catalogo.application.castMember.create

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember

data class CreateCastMemberOutput(
    val id: String
) {

    companion object {
        fun from(aMember: CastMember) = CreateCastMemberOutput(aMember.id.value)
    }
}
