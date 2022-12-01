package com.lukinhasssss.admin.catalogo.domain.validation

interface ValidationHandler {

    fun append(anError: Error): ValidationHandler

    fun append(anHandler: ValidationHandler): ValidationHandler

    // TODO: Tentar entender pq isso funciona em Java mas não em Kotlin
    // fun <T> validate(aValidation: Validation<T>): T

    fun <T> validate(aValidation: () -> T): T?

    fun getErrors(): List<Error>

    fun hasError(): Boolean = getErrors().isNotEmpty()

    fun firstError(): Error = getErrors()[0]

    interface Validation<T> {
        fun validate(): T
    }
}
