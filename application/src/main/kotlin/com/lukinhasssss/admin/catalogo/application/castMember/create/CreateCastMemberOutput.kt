package com.lukinhasssss.admin.catalogo.application.castMember.create

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID

data class CreateCastMemberOutput(
    val id: String
) {

    companion object {
        fun from(aMember: CastMember) = CreateCastMemberOutput(aMember.id.value)

        fun from(anId: CastMemberID) = CreateCastMemberOutput(id = anId.value)
    }
}
