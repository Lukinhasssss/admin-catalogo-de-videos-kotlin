package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils

data class CategoryID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique(): CategoryID {
            return from(anId = IdUtils.uuid())
        }

        fun from(anId: String): CategoryID {
            return CategoryID(value = anId)
        }
    }
}
