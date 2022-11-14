package com.lukinhasssss.admin.catalogo.application.category.update

import com.lukinhasssss.admin.catalogo.domain.category.Category

data class UpdateCategoryOutput(
    val id: String
) {

    companion object {
        fun from(anId: String) = UpdateCategoryOutput(anId)
        fun from(aCategory: Category) = UpdateCategoryOutput(aCategory.id.value)
    }
}
