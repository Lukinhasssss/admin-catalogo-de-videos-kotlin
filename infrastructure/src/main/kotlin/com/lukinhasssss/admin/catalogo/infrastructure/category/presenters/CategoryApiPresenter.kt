package com.lukinhasssss.admin.catalogo.infrastructure.category.presenters

import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.CategoryOutput
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse

fun CategoryOutput.toCategoryResponse() =
    CategoryResponse(
        id = id.value,
        name = name,
        description = description,
        active = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )

// Exemplo de presenter com interface com metodo estatico
interface CategoryApiPresenter {

    companion object {
        fun present(output: CategoryOutput) = with(output) {
            CategoryResponse(
                id = id.value,
                name = name,
                description = description,
                active = isActive,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }
    }
}
