package com.lukinhasssss.admin.catalogo.application.genre.create

import com.lukinhasssss.admin.catalogo.domain.genre.Genre

data class CreateGenreOutput(
    val id: String
) {

    companion object {
        fun from(anId: String) = CreateGenreOutput(anId)
        fun from(aGenre: Genre) = CreateGenreOutput(aGenre.id.value)
    }
}
