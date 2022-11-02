package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import java.time.Instant

class Category(
    override val id: CategoryID,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) : AggregateRoot<CategoryID>(id) {

    companion object {
        fun newCategory(aName: String, aDescription: String, isActive: Boolean): Category {
            val id = CategoryID.unique()
            val now = Instant.now()

            return Category(
                id = id,
                name = aName,
                description = aDescription,
                isActive = isActive,
                createdAt = now,
                updatedAt = now,
                deletedAt = null
            )
        }
    }
}
