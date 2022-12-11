package com.lukinhasssss.admin.catalogo.application.castMember.retrive.list

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import java.time.Instant

data class CastMemberListOutput(
    val id: String,
    val name: String,
    val type: CastMemberType,
    val createdAt: Instant
) {

    companion object {
        fun from(aMember: CastMember) = with(aMember) {
            CastMemberListOutput(
                id = id.value,
                name = name,
                type = type,
                createdAt = createdAt
            )
        }
    }
}
