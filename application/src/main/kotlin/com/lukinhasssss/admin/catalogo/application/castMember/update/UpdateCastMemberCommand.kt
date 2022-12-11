package com.lukinhasssss.admin.catalogo.application.castMember.update

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType

data class UpdateCastMemberCommand(
    val id: String,
    val name: String,
    val type: CastMemberType,
) {

    companion object {
        fun with(
            anId: String,
            aName: String,
            aType: CastMemberType
        ) = UpdateCastMemberCommand(id = anId, name = aName, type = aType)
    }
}
