package com.lukinhasssss.admin.catalogo.application.category.update

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import io.vavr.control.Either
import io.vavr.kotlin.Try
import io.vavr.kotlin.left

class DefaultUpdateCategoryUseCase(
    private val categoryGateway: CategoryGateway
) : UpdateCategoryUseCase() {

    override fun execute(anIn: UpdateCategoryCommand): Either<Notification, UpdateCategoryOutput> {
        with(anIn) {
            val anId = CategoryID.from(id)

            val aCategory = categoryGateway.findById(anId) ?: throw notFound(anId)

            val notification = Notification.create()

            val updatedCategory = aCategory.update(
                aName = name,
                aDescription = description,
                isActive = isActive
            ).apply { validate(notification) }

            return if (notification.hasError()) left(notification) else update(updatedCategory)
        }
    }

    private fun update(aCategory: Category): Either<Notification, UpdateCategoryOutput> {
        return Try { categoryGateway.update(aCategory = aCategory) }
            .toEither()
            .bimap({ Notification.create(it) }, { UpdateCategoryOutput.from(it) })
    }

    private fun notFound(anId: CategoryID) =
        NotFoundException.with(anId, Category::class)
}
