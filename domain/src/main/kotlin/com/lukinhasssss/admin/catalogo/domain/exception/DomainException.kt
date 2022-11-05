package com.lukinhasssss.admin.catalogo.domain.exception

import com.lukinhasssss.admin.catalogo.domain.validation.Error

data class DomainException(
    override val message: String?,
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
