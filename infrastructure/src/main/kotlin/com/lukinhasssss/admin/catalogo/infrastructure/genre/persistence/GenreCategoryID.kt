package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class GenreCategoryID(

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
}

/**
 * Embeddable: Serve para dizer que a 'classe' vai ser colocada inteira
 *             dentro de quem vai utilizá-la. Para dizer que todos os atributos nela vao ser
 *             atributos raiz da entidade que mapear ela.
 */
