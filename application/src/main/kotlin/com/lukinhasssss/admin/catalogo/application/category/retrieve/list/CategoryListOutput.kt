package com.lukinhasssss.admin.catalogo.application.category.retrieve.list

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import java.time.Instant

data class CategoryListOutput(
    val id: CategoryID,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Instant,
    val deletedAt: Instant?
) {

    companion object {
        fun from(aCategory: Category): CategoryListOutput {
            return with(aCategory) {
                CategoryListOutput(
                    id = id,
                    name = name,
                    description = description,
                    isActive = isActive,
                    createdAt = createdAt,
                    deletedAt = deletedAt
                )
            }
        }
    }
}
