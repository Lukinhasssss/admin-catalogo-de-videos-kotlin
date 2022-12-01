package com.lukinhasssss.admin.catalogo.domain.validation.handler

import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler

class Notification(
    private val errors: MutableList<Error>,
) : ValidationHandler {

    companion object {
        fun create(): Notification {
            return Notification(mutableListOf())
        }

        fun create(anError: Error): Notification {
            return Notification(mutableListOf()).append(anError)
        }

        fun create(t: Throwable): Notification {
            return create(Error(t.message))
        }
    }

    override fun append(anError: Error): Notification {
        errors.add(anError)
        return this
    }

    override fun append(anHandler: ValidationHandler): ValidationHandler {
        errors.addAll(anHandler.getErrors())
        return this
    }

    // TODO: Tentar entender pq isso funciona em Java mas não em Kotlin
    /*override fun <T> validate(aValidation: ValidationHandler.Validation<T>): T {
        try {
            return aValidation.validate()
        } catch (ex: DomainException) {
            errors.addAll(ex.errors)
        } catch (t: Throwable) {
            errors.add(Error(t.message))
        }

        return aValidation.validate()
    }*/

    override fun <T> validate(aValidation: () -> T): T? {
        try {
            return aValidation.invoke()
        } catch (ex: DomainException) {
            errors.addAll(ex.errors)
        } catch (t: Throwable) {
            errors.add(Error(t.message))
        }

        return null
    }

    override fun getErrors(): List<Error> = errors
}
