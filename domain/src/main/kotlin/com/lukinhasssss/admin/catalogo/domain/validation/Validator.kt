package com.lukinhasssss.admin.catalogo.domain.validation

abstract class Validator(
    val validationHandler: ValidationHandler
) {
    abstract fun validate()
}
