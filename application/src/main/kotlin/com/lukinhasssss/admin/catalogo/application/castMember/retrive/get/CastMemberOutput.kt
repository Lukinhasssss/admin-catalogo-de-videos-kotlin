package com.lukinhasssss.admin.catalogo.application.castMember.retrive.get

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import java.time.Instant

data class CastMemberOutput(
    val id: String,
    val name: String,
    val type: CastMemberType,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    companion object {
        fun from(aMember: CastMember) = with(aMember) {
            CastMemberOutput(
                id = id.value,
                name = name,
                type = type,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
