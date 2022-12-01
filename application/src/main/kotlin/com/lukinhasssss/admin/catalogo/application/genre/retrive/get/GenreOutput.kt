package com.lukinhasssss.admin.catalogo.application.genre.retrive.get

import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import java.time.Instant

data class GenreOutput(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val categories: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) {

    companion object {
        fun from(aGenre: Genre): GenreOutput {
            with(aGenre) {
                return GenreOutput(
                    id = id.value,
                    name = name,
                    isActive = isActive(),
                    categories = categories.map { it.value },
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    deletedAt = deletedAt
                )
            }
        }
    }
}
