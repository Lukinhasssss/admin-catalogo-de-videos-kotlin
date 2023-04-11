package com.lukinhasssss.admin.catalogo.infrastructure.utils

object SqlUtils {

    fun upper(term: String): String? {
        if (term.isEmpty()) {
            return null
        }

        return "%${term.uppercase()}%"
    }

    fun like(term: String): String = "%$term%"
}
