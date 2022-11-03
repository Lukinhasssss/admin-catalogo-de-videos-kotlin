package com.lukinhasssss.admin.catalogo.domain.validation

abstract class Validator(
    val handler: ValidationHandler
) {
    abstract fun validate()
}
