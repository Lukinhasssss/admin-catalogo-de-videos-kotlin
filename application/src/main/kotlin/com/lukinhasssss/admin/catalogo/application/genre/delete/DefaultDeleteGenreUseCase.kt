package com.lukinhasssss.admin.catalogo.application.genre.delete

import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID

class DefaultDeleteGenreUseCase(
    private val genreGateway: GenreGateway
) : DeleteGenreUseCase() {

    override fun execute(anIn: String) {
        genreGateway.deleteById(GenreID.from(anIn))
    }
}
