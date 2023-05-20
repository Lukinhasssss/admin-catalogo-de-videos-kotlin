package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.Validator

class GenreValidator(
    private val genre: Genre,
    validationHandler: ValidationHandler
) : Validator(validationHandler) {

    override fun validate() {
        checkNameConstraints()
    }

    companion object {
        const val MIN_NAME_LENGTH = 1
        const val MAX_NAME_LENGTH = 255
    }

    private fun checkNameConstraints() {
        with(genre.name) {
            if (isNullOrBlank()) {
                validationHandler.append(Error(message = "'name' should not be empty"))
                return
            }

            val nameLength = trim().length

            if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
                validationHandler.append(Error(message = "'name' must be between 1 and 255 characters"))
            }
        }
    }
}
