package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "category")
class CategoryJpaEntity(

    @Id
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", length = 4000)
    val description: String?,

    @Column(name = "active", nullable = false)
    val isActive: Boolean,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val updatedAt: Instant,

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP(6)")
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
