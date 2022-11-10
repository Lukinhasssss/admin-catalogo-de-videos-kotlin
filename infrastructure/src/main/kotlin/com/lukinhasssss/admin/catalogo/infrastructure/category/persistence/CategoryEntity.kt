package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "category")
class CategoryEntity(

    @Id
    val id: String,

    @Column(value = "name")
    val name: String,

    @Column(value = "description")
    val description: String?,

    @Column(value = "active")
    val isActive: Boolean,

    @Column(value = "created_at")
    val createdAt: Instant,

    @Column(value = "updated_at")
    val updatedAt: Instant,

    @Column(value = "deleted_at")
    val deletedAt: Instant?
) {

    companion object {
        fun from(aCategory: Category): CategoryEntity {
            return with(aCategory) {
                CategoryEntity(
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
