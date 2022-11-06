package com.lukinhasssss.admin.catalogo.application.category.delete

import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID

class DefaultDeleteCategoryUseCase(
    private val categoryGateway: CategoryGateway
) : DeleteCategoryUseCase() {

    override fun execute(anIn: String) {
        categoryGateway.deleteById(CategoryID.from(anIn))
    }
}
