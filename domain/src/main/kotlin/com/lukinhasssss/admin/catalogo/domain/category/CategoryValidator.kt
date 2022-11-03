package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.Validator

class CategoryValidator(
    val category: Category,
    handler: ValidationHandler
) : Validator(handler) {

    override fun validate() {
        if (category.name.isBlank())
            handler.append(Error(message = "'name' should not be empty"))
    }
}
