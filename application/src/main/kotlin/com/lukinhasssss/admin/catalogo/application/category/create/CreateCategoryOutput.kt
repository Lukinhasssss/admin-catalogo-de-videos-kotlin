package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.domain.category.Category

data class CreateCategoryOutput(
    val id: String
) {

    companion object {
        fun from(anId: String) = CreateCategoryOutput(anId)
        fun from(aCategory: Category) = CreateCategoryOutput(aCategory.id.value)
    }
}
