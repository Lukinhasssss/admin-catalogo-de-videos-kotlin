package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class GenreCategoryID(

    @Column(name = "genre_id", nullable = false)
    val genreId: String,

    @Column(name = "category_id", nullable = false)
    val categoryId: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 6770797286514298466L

        fun from(aGenreId: String, aCategoryId: String) =
            GenreCategoryID(genreId = aGenreId, categoryId = aCategoryId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenreCategoryID

        if (genreId != other.genreId) return false
        if (categoryId != other.categoryId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = genreId.hashCode()
        result = 31 * result + categoryId.hashCode()
        return result
    }
}

/**
 * Embeddable: Serve para dizer que a 'classe' vai ser colocada inteira
 *             dentro de quem vai utilizá-la. Para dizer que todos os atributos nela vao ser
 *             atributos raiz da entidade que mapear ela.
 */
