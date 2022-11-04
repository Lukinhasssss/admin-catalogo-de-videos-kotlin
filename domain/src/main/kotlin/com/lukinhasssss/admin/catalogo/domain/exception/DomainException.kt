package com.lukinhasssss.admin.catalogo.domain.exception

import com.lukinhasssss.admin.catalogo.domain.validation.Error

class DomainException(
    message: String?,
    val errors: List<Error>
) : NoStacktraceException(message) {

    companion object {
        fun with(anError: Error): DomainException {
            return DomainException(message = anError.message, errors = listOf(anError))
        }

        fun with(anErrors: List<Error>): DomainException {
            return DomainException(message = "", errors = anErrors)
        }
    }
}
