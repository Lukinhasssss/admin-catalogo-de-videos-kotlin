package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(name = "genres_categories")
data class GenreCategoryJpaEntity(

    @EmbeddedId
    val id: GenreCategoryID,

    @ManyToOne
    @MapsId(value = "genreId")
    val genre: GenreJpaEntity
) {

    constructor(aCategoryID: CategoryID, aGenre: GenreJpaEntity) : this(
        id = GenreCategoryID.from(aGenreId = aGenre.id, aCategoryId = aCategoryID.value),
        genre = aGenre
    )

    companion object {
        fun from(aCategoryID: CategoryID, aGenre: GenreJpaEntity) =
            GenreCategoryJpaEntity(aCategoryID = aCategoryID, aGenre = aGenre)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenreCategoryJpaEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
