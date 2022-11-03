package com.lukinhasssss.admin.catalogo.domain.validation.handler

import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler

class ThrowsValidationHandler : ValidationHandler {

    override fun append(anError: Error): ValidationHandler {
        throw DomainException.with(listOf(anError))
    }

    override fun append(anHandler: ValidationHandler): ValidationHandler {
        throw DomainException.with(anHandler.getErrors())
    }

    override fun validate(aValidation: ValidationHandler.Validation): ValidationHandler {
        try {
            aValidation.validate()
        } catch (ex: Exception) {
            throw DomainException.with(listOf(Error(ex.message)))
        }

        return this
    }

    override fun getErrors(): List<Error> {
        return emptyList()
    }
}
