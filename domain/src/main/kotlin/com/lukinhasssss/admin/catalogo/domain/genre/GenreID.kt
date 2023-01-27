package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils

data class GenreID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique(): GenreID {
            return from(anId = IdUtils.uuid())
        }

        fun from(anId: String): GenreID {
            return GenreID(value = anId)
        }
    }
}
