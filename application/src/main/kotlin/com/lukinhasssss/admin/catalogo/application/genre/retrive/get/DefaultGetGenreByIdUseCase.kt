package com.lukinhasssss.admin.catalogo.application.genre.retrive.get

import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID

class DefaultGetGenreByIdUseCase(
    private val genreGateway: GenreGateway
) : GetGenreByIdUseCase() {

    override fun execute(anIn: String): GenreOutput {
        val anId = GenreID.from(anIn)

        return genreGateway.findById(anId)?.map() ?: throw notFound(anId)
    }

    private fun Genre.map() =
        GenreOutput(
            id = id.value,
            name = name,
            isActive = isActive(),
            categories = categories.map { it.value },
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt
        )

    private fun notFound(anGenreID: GenreID) =
        NotFoundException.with(
            id = anGenreID,
            anAggregate = Genre::class
        )
}
