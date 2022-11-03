package com.lukinhasssss.admin.catalogo.domain.exception

import com.lukinhasssss.admin.catalogo.domain.validation.Error

class DomainException(
    val errors: List<Error>
) : RuntimeException("", null, true, false) {

    companion object {
        fun with(anErrors: List<Error>): DomainException {
            return DomainException(errors = anErrors)
        }
    }
}
