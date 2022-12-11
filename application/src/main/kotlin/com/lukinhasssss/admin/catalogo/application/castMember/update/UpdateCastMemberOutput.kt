package com.lukinhasssss.admin.catalogo.application.castMember.update

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember

data class UpdateCastMemberOutput(
    val id: String
) {

    companion object {
        fun from(aMember: CastMember) = UpdateCastMemberOutput(aMember.id.value)
    }
}
