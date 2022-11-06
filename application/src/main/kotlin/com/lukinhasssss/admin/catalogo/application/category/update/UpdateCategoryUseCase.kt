package com.lukinhasssss.admin.catalogo.application.category.update

import com.lukinhasssss.admin.catalogo.application.UseCase
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import io.vavr.control.Either

abstract class UpdateCategoryUseCase : UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>>()
