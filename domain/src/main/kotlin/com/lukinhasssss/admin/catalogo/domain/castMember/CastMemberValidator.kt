package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.Validator

class CastMemberValidator(
    private val castMember: CastMember,
    validationHandler: ValidationHandler
) : Validator(validationHandler) {

    override fun validate() = checkNameConstraints()

    companion object {
        private const val MIN_NAME_LENGTH = 3
        private const val MAX_NAME_LENGTH = 255
    }

    private fun checkNameConstraints() {
        with(castMember.name) {
            if (isNullOrBlank()) {
                validationHandler.append(Error(message = "'name' should not be empty"))
                return
            }

            val nameLength = trim().length

            if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
                validationHandler.append(Error(message = "'name' must be between 3 and 255 characters"))
            }
        }
    }
}
