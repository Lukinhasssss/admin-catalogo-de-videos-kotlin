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
        fun from(aGenreId: String, aCategoryId: String) =
            GenreCategoryID(genreId = aGenreId, categoryId = aCategoryId)
    }
}

/**
 * Embeddable: Serve para dizer que a 'classe' vai ser colocada inteira
 *             dentro de quem vai utilizá-la. Para dizer que todos os atributos nela vao ser
 *             atributos raiz da entidade que mapear ela.
 */
