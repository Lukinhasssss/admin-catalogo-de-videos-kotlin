package com.lukinhasssss.admin.catalogo.domain.validation

interface ValidationHandler {

    fun append(anError: Error): ValidationHandler

    fun append(anHandler: ValidationHandler): ValidationHandler

    fun validate(aValidation: Validation): ValidationHandler

    fun getErrors(): List<Error>

    fun hasError(): Boolean = getErrors().isNotEmpty()

    fun firstError(): Error = getErrors()[0]

    interface Validation {
        fun validate()
    }
}
