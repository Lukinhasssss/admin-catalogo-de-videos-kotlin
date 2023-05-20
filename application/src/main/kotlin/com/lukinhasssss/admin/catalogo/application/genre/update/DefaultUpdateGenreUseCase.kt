package com.lukinhasssss.admin.catalogo.application.genre.update

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification

class DefaultUpdateGenreUseCase(
    private val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway
) : UpdateGenreUseCase() {

    override fun execute(anIn: UpdateGenreCommand): UpdateGenreOutput {
        with(anIn) {
            val anId = GenreID.from(id)
            val categories = categories.toCategoryID()

            val aGenre = genreGateway.findById(anId) ?: throw notFound(anId)

            val notification = Notification.create()
            notification.append(validateCategories(categories))

            val aGenreUpdated = notification.validate {
                aGenre.update(
                    aName = name,
                    isActive = isActive,
                    categories = categories.toMutableList()
                )
            }

            if (notification.hasError()) {
                throw NotificationException("Could not update Aggregate Genre $id", notification)
            }

            return UpdateGenreOutput.from(genreGateway.update(aGenreUpdated!!))
        }
    }

    private fun validateCategories(ids: List<CategoryID>): ValidationHandler {
        val notification = Notification.create()

        if (ids.isEmpty()) return notification

        val retrievedIds = categoryGateway.existsByIds(ids)

        if (ids.size != retrievedIds.size) {
            val missingIds = ids.toMutableList()
            missingIds.removeAll(retrievedIds)

            val missingIdsMessage = missingIds.joinToString(separator = ", ") { it.value }
            // val missingIdsMessage = missingIds.map { it.value }.joinToString(separator = ", ")

            notification.append(Error("Some categories could not be found: $missingIdsMessage"))
        }

        return notification
    }

    private fun notFound(anId: Identifier) =
        NotFoundException.with(anId, Genre::class)

    private fun List<String>.toCategoryID() = map { CategoryID.from(it) }
}
