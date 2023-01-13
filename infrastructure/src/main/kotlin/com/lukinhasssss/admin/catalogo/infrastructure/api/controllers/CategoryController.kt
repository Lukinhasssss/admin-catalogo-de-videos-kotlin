package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryCommand
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.delete.DeleteCategoryUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase
import com.lukinhasssss.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryCommand
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryOutput
import com.lukinhasssss.admin.catalogo.application.category.update.UpdateCategoryUseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.api.CategoryAPI
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.presenters.toCategoryListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.presenters.toCategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.function.Function

@RestController
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val listCategoriesUseCase: ListCategoriesUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : CategoryAPI {

    override fun createCategory(request: CreateCategoryRequest): ResponseEntity<Any> {
        Logger.info(message = "Iniciando processo de criação de categoria...", payload = request)

        val aCommand = with(request) {
            CreateCategoryCommand.with(
                aName = name,
                aDescription = description,
                isActive = isActive()
            )
        }

        val onError = Function<Notification, ResponseEntity<Any>> {
            Logger.warning(message = "Não foi possível criar a categoria!", payload = it)
            ResponseEntity.unprocessableEntity().body(it)
        }

        val onSuccess = Function<CreateCategoryOutput, ResponseEntity<Any>> {
            Logger.info(message = "Finalizado processo de criação de categoria!")
            ResponseEntity.created(URI.create("/categories/${it.id}")).body(it)
        }

        return createCategoryUseCase.execute(aCommand).fold(onError, onSuccess)
    }

    override fun getById(id: String): CategoryResponse {
        Logger.info(message = "Iniciando processo de busca de categoria...")

        return getCategoryByIdUseCase.execute(id).toCategoryResponse().also {
            Logger.info(message = "Finalizado processo de busca de categoria!")
        }
    }

    override fun listCategories(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String
    ): Pagination<CategoryListResponse> {
        Logger.info(message = "Iniciando processo de listagem de categorias...")

        val aQuery = SearchQuery(page, perPage, search, sort, direction)

        Logger.info(message = "Finalizado processo de listagem de categorias")
        return listCategoriesUseCase.execute(aQuery).map { it.toCategoryListResponse() }
    }

    override fun updateById(id: String, request: UpdateCategoryRequest): ResponseEntity<Any> {
        Logger.info(message = "Iniciando processo de atualização de categoria...", payload = request)

        val aCommand = with(request) {
            UpdateCategoryCommand.with(
                anId = id,
                aName = name,
                aDescription = description,
                isActive = isActive()
            )
        }

        val onError = Function<Notification, ResponseEntity<Any>> {
            Logger.warning(message = "Não foi possível atualizar a categoria!")
            ResponseEntity.unprocessableEntity().body(it)
        }

        val onSuccess = Function<UpdateCategoryOutput, ResponseEntity<Any>> {
            Logger.info(message = "Finalizado processo de atualização de categoria!")
            ResponseEntity.ok(it)
        }

        return updateCategoryUseCase.execute(aCommand).fold(onError, onSuccess)
    }

    override fun deleteById(id: String) {
        Logger.info(message = "Iniciando processo de deleção de categoria...")

        deleteCategoryUseCase.execute(id).also {
            Logger.info(message = "Finalizado processo de deleção de categoria!")
        }
    }
}
