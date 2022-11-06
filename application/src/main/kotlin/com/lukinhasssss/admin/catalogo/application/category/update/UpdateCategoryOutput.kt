package com.lukinhasssss.admin.catalogo.application.category.update

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID

data class UpdateCategoryOutput(
    val id: CategoryID
) {

    companion object {
        fun from(aCategory: Category) = UpdateCategoryOutput(aCategory.id)
    }
}
