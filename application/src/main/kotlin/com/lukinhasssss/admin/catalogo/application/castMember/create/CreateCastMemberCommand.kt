package com.lukinhasssss.admin.catalogo.application.castMember.create

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType

data class CreateCastMemberCommand(
    val name: String,
    val type: CastMemberType
) {

    companion object {
        fun with(aName: String, aType: CastMemberType) =
            CreateCastMemberCommand(name = aName, type = aType)
    }
}
