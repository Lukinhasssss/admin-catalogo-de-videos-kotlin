package com.lukinhasssss.admin.catalogo.domain.validation.handler

import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler

class ThrowsValidationHandler : ValidationHandler {

    override fun append(anError: Error): ValidationHandler {
        throw DomainException.with(anError)
    }

    override fun append(anHandler: ValidationHandler): ValidationHandler {
        throw DomainException.with(anHandler.getErrors())
    }

    // TODO: Tentar entender pq isso funciona em Java mas não em Kotlin
    override fun <T> validate(aValidation: ValidationHandler.Validation<T>): T {
        try {
            return aValidation.validate()
        } catch (ex: Exception) {
            throw DomainException.with(listOf(Error(ex.message)))
        }
    }

    override fun <T> validate(aValidation: () -> T): T {
        try {
            return aValidation.invoke()
        } catch (ex: Exception) {
            throw DomainException.with(listOf(Error(ex.message)))
        }
    }

    override fun getErrors() = emptyList<Error>()
}
