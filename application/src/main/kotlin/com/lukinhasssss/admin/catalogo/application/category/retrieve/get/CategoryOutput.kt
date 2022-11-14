package com.lukinhasssss.admin.catalogo.application.category.retrieve.get

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import java.time.Instant

data class CategoryOutput(
    val id: CategoryID,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) {

    companion object {
        fun from(aCategory: Category): CategoryOutput {
            with(aCategory) {
                return CategoryOutput(
                    id = id,
                    name = name,
                    description = description,
                    isActive = isActive,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    deletedAt = deletedAt
                )
            }
        }
    }
}
