package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import java.time.Instant

class Genre(
    override val id: GenreID,
    val name: String,
    val active: Boolean,
    val categories: List<CategoryID>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) : AggregateRoot<GenreID>(id) {

    init {
        val notification = Notification.create()
        validate(notification)

        if (notification.hasError())
            throw NotificationException(notification = notification)
    }

    companion object {
        fun newGenre(aName: String, isActive: Boolean): Genre {
            val anId = GenreID.unique()
            val now = Instant.now()
            val deletedAt = if (isActive) null else now

            return Genre(anId, aName, isActive, mutableListOf(), now, now, deletedAt)
        }

        fun with(
            anId: GenreID,
            aName: String,
            isActive: Boolean,
            categories: List<CategoryID>,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?
        ) = Genre(anId, aName, isActive, categories, createdAt, updatedAt, deletedAt)

        fun with(aGenre: Genre) = with(aGenre) {
            Genre(id, name, active, categories, createdAt, updatedAt, deletedAt)
        }
    }

    fun isActive() = active

    override fun validate(handler: ValidationHandler) =
        GenreValidator(this, handler).validate()
}
