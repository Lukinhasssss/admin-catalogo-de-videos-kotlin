package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryCommand
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.delete.DeleteCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryCommand
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryUseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.api.CategoryAPI
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.presenters.toCategoryResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.function.Function

@RestController
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : CategoryAPI {

    override fun createCategory(request: CreateCategoryRequest): ResponseEntity<Any> {
        val aCommand = with(request) {
            CreateCategoryCommand.with(
                aName = name,
                aDescription = description,
                isActive = active
            )
        }

        val onError = Function<Notification, ResponseEntity<Any>> {
            ResponseEntity.unprocessableEntity().body(it)
        }

        val onSuccess = Function<CreateCategoryOutput, ResponseEntity<Any>> {
            ResponseEntity.created(URI.create("/categories/${it.id}")).body(it)
        }

        return createCategoryUseCase.execute(aCommand).fold(onError, onSuccess)
    }

    override fun getById(id: String): CategoryResponse {
        val categoryOutput = getCategoryByIdUseCase.execute(id)

        return categoryOutput.toCategoryResponse()
    }

    override fun listCategories(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String
    ): Pagination<Any> {
        TODO("Not yet implemented")
    }

    override fun updateById(id: String, request: UpdateCategoryRequest): ResponseEntity<Any> {
        val aCommand = with(request) {
            UpdateCategoryCommand.with(
                anId = id,
                aName = name,
                aDescription = description,
                isActive = active
            )
        }

        val onError = Function<Notification, ResponseEntity<Any>> {
            ResponseEntity.unprocessableEntity().body(it)
        }

        val onSuccess = Function<UpdateCategoryOutput, ResponseEntity<Any>> { ResponseEntity.ok(it) }

        return updateCategoryUseCase.execute(aCommand).fold(onError, onSuccess)
    }

    override fun deleteById(id: String) = deleteCategoryUseCase.execute(id)
}
