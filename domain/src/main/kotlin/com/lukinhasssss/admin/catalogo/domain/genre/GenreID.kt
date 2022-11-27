package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.Identifier
import java.util.UUID

data class GenreID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique(): GenreID {
            return from(anId = UUID.randomUUID())
        }

        fun from(anId: String): GenreID {
            return GenreID(value = anId)
        }

        fun from(anId: UUID): GenreID {
            return GenreID(value = anId.toString().lowercase())
        }
    }
}
