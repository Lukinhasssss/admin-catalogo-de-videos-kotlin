package com.lukinhasssss.admin.catalogo.application.genre.retrive.list

import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

class DefaultListGenreUseCase(
    private val genreGateway: GenreGateway
) : ListGenreUseCase() {

    override fun execute(anIn: SearchQuery): Pagination<GenreListOutput> {
        return genreGateway.findAll(anIn).map { GenreListOutput.from(it) }
    }
}
