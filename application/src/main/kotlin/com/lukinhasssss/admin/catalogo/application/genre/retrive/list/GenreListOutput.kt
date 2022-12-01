package com.lukinhasssss.admin.catalogo.application.genre.retrive.list

import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import java.time.Instant

data class GenreListOutput(
    val name: String,
    val isActive: Boolean,
    val categories: List<String>,
    val createdAt: Instant,
    val deletedAt: Instant?
) {

    companion object {
        fun from(aGenre: Genre): GenreListOutput {
            with(aGenre) {
                return GenreListOutput(
                    name = name,
                    isActive = isActive(),
                    categories = categories.map { it.value },
                    createdAt = createdAt,
                    deletedAt = deletedAt
                )
            }
        }
    }
}
