package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import java.time.Instant

data class Genre(
    override val id: GenreID,
    val name: String,
    val active: Boolean,
    val categories: MutableList<CategoryID>,
    val createdAt: Instant,
    var updatedAt: Instant,
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
            val now = InstantUtils.now()
            val deletedAt = if (isActive) null else now

            return Genre(anId, aName, isActive, mutableListOf(), now, now, deletedAt)
        }

        fun with(
            anId: GenreID,
            aName: String,
            isActive: Boolean,
            categories: MutableList<CategoryID>,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?
        ) = Genre(anId, aName, isActive, categories, createdAt, updatedAt, deletedAt)

        fun with(aGenre: Genre) = with(aGenre) {
            Genre(id, name, active, categories, createdAt, updatedAt, deletedAt)
        }
    }

    fun isActive() = active

    override fun validate(handler: ValidationHandler) = GenreValidator(this, handler).validate()

    fun activate() = Genre(
        id = id,
        name = name,
        active = true,
        categories = categories,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        deletedAt = null
    )

    fun deactivate() = Genre(
        id = id,
        name = name,
        active = false,
        categories = categories,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        deletedAt = deletedAt ?: InstantUtils.now()
    )

    fun update(
        aName: String,
        isActive: Boolean,
        categories: MutableList<CategoryID>? = mutableListOf()
    ) = Genre(
        id = id,
        name = aName,
        active = if (isActive) activate().isActive() else deactivate().isActive(),
        categories = categories ?: mutableListOf(),
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        deletedAt = if (isActive) activate().deletedAt else deactivate().deletedAt
    )

    fun addCategory(aCategoryID: CategoryID): Genre {
        categories.add(aCategoryID)
        updatedAt = InstantUtils.now()
        return this
    }

    fun addCategories(categories: List<CategoryID>): Genre {
        if (categories.isEmpty())
            return this

        this.categories.addAll(categories)
        updatedAt = InstantUtils.now()
        return this
    }

    fun removeCategory(aCategoryID: CategoryID): Genre {
        categories.remove(aCategoryID)
        updatedAt = InstantUtils.now()
        return this
    }
}
