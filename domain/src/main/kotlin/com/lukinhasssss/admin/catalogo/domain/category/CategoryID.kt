package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.Identifier
import java.util.UUID

data class CategoryID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique(): CategoryID {
            return from(anId = UUID.randomUUID())
        }

        fun from(anId: String): CategoryID {
            return CategoryID(value = anId)
        }

        fun from(anId: UUID): CategoryID {
            return CategoryID(value = anId.toString().lowercase())
        }
    }
}
