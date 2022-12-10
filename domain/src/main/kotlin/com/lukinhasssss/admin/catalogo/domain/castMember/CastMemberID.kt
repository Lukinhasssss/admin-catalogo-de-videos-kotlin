package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.Identifier
import java.util.UUID

data class CastMemberID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique(): CastMemberID {
            return from(anId = UUID.randomUUID())
        }

        fun from(anId: String): CastMemberID {
            return CastMemberID(value = anId)
        }

        fun from(anId: UUID): CastMemberID {
            return CastMemberID(value = anId.toString().lowercase())
        }
    }
}
