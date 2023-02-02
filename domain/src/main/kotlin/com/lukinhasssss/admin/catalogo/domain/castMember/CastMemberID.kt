package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils

data class CastMemberID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique(): CastMemberID {
            return from(anId = IdUtils.uuid())
        }

        fun from(anId: String): CastMemberID {
            return CastMemberID(value = anId)
        }
    }
}
