package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
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
                deletedAt = if (isActive) null else now
            )
        }
    }

    override fun validate(handler: ValidationHandler) {
        CategoryValidator(category = this, validationHandler = handler).validate()
    }

    fun activate(): Category {
        return Category(
            id = id,
            name = name,
            description = description,
            isActive = true,
            createdAt = createdAt,
            updatedAt = Instant.now(),
            deletedAt = null
        )
    }

    fun deactivate(): Category {
        return Category(
            id = id,
            name = name,
            description = description,
            isActive = false,
            createdAt = createdAt,
            updatedAt = Instant.now(),
            deletedAt = deletedAt ?: Instant.now()
        )
    }

    fun update(aName: String, aDescription: String, isActive: Boolean): Category {
        return Category(
            id = id,
            name = aName,
            description = aDescription,
            isActive = if (isActive) activate().isActive else deactivate().isActive,
            createdAt = createdAt,
            updatedAt = Instant.now(),
            deletedAt = if (isActive) activate().deletedAt else deactivate().deletedAt
        )
    }
}
