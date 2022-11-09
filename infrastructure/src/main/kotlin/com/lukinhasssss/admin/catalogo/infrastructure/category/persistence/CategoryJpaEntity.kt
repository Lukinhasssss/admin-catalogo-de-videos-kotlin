package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "category")
class CategoryJpaEntity(

    @Id
    val id: String,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) {

    companion object {
        fun from(aCategory: Category): CategoryJpaEntity {
            return with(aCategory) {
                CategoryJpaEntity(
                    id = id.value,
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

    fun toAggregate() =
        Category.with(
            anId = CategoryID.from(id),
            aName = name,
            aDescription = description,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt
        )
}
