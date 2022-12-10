package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.Validator

class CastMemberValidator(
    private val castMember: CastMember,
    validationHandler: ValidationHandler
) : Validator(validationHandler) {

    override fun validate() {}
}
