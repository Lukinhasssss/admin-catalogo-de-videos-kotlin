package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import io.vavr.control.Either
import io.vavr.kotlin.Try
import io.vavr.kotlin.left

class DefaultCreateCategoryUseCase(
    private val categoryGateway: CategoryGateway
) : CreateCategoryUseCase() {

    override fun execute(aCommand: CreateCategoryCommand): Either<Notification, CreateCategoryOutput> {
        with(aCommand) {
            val notification = Notification.create()

            val aCategory = Category.newCategory(
                aName = name,
                aDescription = description,
                isActive = isActive
            ).apply { validate(notification) }

            return if (notification.hasError()) left(notification) else create(aCategory)
        }
    }

    private fun create(aCategory: Category): Either<Notification, CreateCategoryOutput> {
        return Try { categoryGateway.create(aCategory) }
            .toEither()
            .bimap({ Notification.create(it) }, { CreateCategoryOutput.from(it) })
    }
}

/*
    # Outra forma de fazer a funcao create

    private fun create(aCategory: Category): Either<Notification, CreateCategoryOutput> {
        return Try { categoryGateway.create(aCategory) }
            .toEither()
            .mapLeft {Notification.create(it) }
            .map { CreateCategoryOutput.from(it) }
    }
 */
