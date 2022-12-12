package com.lukinhasssss.admin.catalogo.application.castMember.update

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID

data class UpdateCastMemberOutput(
    val id: String
) {

    companion object {
        fun from(anId: CastMemberID) = UpdateCastMemberOutput(anId.value)
        fun from(aMember: CastMember) = UpdateCastMemberOutput(aMember.id.value)
    }
}
