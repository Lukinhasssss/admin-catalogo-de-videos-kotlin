package com.lukinhasssss.admin.catalogo.application.genre.update

import com.lukinhasssss.admin.catalogo.domain.genre.Genre

data class UpdateGenreOutput(
    val id: String
) {

    companion object {
        fun from(anId: String) = UpdateGenreOutput(anId)
        fun from(aGenre: Genre) = UpdateGenreOutput(aGenre.id.value)
    }
}
