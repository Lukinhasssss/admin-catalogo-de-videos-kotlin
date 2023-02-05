package com.lukinhasssss.admin.catalogo.domain.exception

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import kotlin.reflect.KClass

class NotFoundException(
    message: String?,
    errors: List<Error>
) : DomainException(message, errors) {

    companion object {
        fun with(
            id: Identifier,
            anAggregate: KClass<out AggregateRoot<*>>
        ): NotFoundException {
            val anErrorMessage = "${anAggregate.simpleName} with ID ${id.value} was not found"

            return NotFoundException(anErrorMessage, emptyList())
        }

        fun with(error: Error) = NotFoundException(error.message, listOf(error))
    }
}
