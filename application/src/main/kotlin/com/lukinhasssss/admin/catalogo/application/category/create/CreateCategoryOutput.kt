package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID

data class CreateCategoryOutput(
    val id: CategoryID
) {

    companion object {
        fun from(aCategory: Category) = CreateCategoryOutput(aCategory.id)
    }
}
