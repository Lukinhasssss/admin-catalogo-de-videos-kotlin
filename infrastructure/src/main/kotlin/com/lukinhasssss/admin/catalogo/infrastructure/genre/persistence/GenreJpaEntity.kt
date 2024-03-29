package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity(name = "Genre")
@Table(name = "genres")
class GenreJpaEntity(

    @Id
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "active", nullable = false)
    val active: Boolean,

    @OneToMany(mappedBy = "genre", cascade = [ALL], fetch = EAGER, orphanRemoval = true)
    val categories: MutableSet<GenreCategoryJpaEntity> = mutableSetOf(),

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val updatedAt: Instant,

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP(6)")
    val deletedAt: Instant?
) {

    companion object {
        fun from(aGenre: Genre): GenreJpaEntity = with(aGenre) {
            val anEntity = GenreJpaEntity(
                id = id.value,
                name = name,
                active = isActive(),
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )

            this.categories.forEach { anEntity.addCategory(it) }

            return anEntity
        }
    }

    fun toAggregate() = Genre.with(
        anId = GenreID.from(id),
        aName = name,
        isActive = active,
        categories = getCategoryIDs(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )

    fun addCategory(anId: CategoryID) =
        categories.add(GenreCategoryJpaEntity.from(aCategoryID = anId, aGenre = this))

    fun removeCategory(anId: CategoryID) =
        categories.remove(GenreCategoryJpaEntity.from(aCategoryID = anId, aGenre = this))

    fun getCategoryIDs() = categories.map { CategoryID.from(it.id.categoryId) }.toMutableList()
}
