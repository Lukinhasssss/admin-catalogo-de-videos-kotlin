package com.lukinhasssss.admin.catalogo.domain.validation.handler

import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler.Validation

class Notification(
    private val errors: MutableList<Error>
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

    override fun append(anHandler: ValidationHandler): Notification {
        errors.addAll(anHandler.getErrors())
        return this
    }

    override fun validate(aValidation: Validation): Notification {
        try {
            aValidation.validate()
        } catch (ex: DomainException) {
            errors.addAll(ex.errors)
        } catch (t: Throwable) {
            errors.add(Error(t.message))
        }

        return this
    }

    override fun getErrors(): List<Error> = errors
}
