package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [MissingKotlinParameterException::class])
    fun handleMissingKotlinParameterException(ex: MissingKotlinParameterException) =
        ResponseEntity.badRequest().body(ApiError.from(ex))

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(ex: NotFoundException) =
        ResponseEntity.status(NOT_FOUND).body(ApiError.from(ex))

    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(ex: DomainException) =
        ResponseEntity.unprocessableEntity().body(ApiError.from(ex))

    data class ApiError(val message: String? = "", val errors: List<Error> = emptyList()) {
        companion object {
            fun from(ex: DomainException) = with(ex) {
                ApiError(message, errors)
            }

            fun from(ex: MissingKotlinParameterException) = with(ex) {
                val errors = path.map {
                    val message = "'${it.fieldName}' should not be null"
                    Error(message)
                }

                ApiError(errors = errors)
            }
        }
    }
}
