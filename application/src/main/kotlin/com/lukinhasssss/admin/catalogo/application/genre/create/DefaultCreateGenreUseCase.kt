package com.lukinhasssss.admin.catalogo.application.genre.create

import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification

class DefaultCreateGenreUseCase(
    private val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway
) : CreateGenreUseCase() {

    override fun execute(anIn: CreateGenreCommand): CreateGenreOutput {
        with(anIn) {
            val categories = categories.toCategoryID()

            val notification = Notification.create()
            notification.append(validateCategories(categories))

            val aGenre = notification.validate { Genre.newGenre(name, isActive) }

            if (notification.hasError()) {
                throw NotificationException("Could not create Aggregate Genre", notification)
            }

            aGenre?.addCategories(categories)

            return CreateGenreOutput.from(genreGateway.create(aGenre!!))
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

    private fun List<String>.toCategoryID() = map { CategoryID.from(it) }
}
