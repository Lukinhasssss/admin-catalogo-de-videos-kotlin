package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.application.UseCase
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import io.vavr.control.Either

abstract class CreateCategoryUseCase : UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>>()
