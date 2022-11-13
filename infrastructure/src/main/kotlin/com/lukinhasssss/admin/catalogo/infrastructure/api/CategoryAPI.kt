package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.lukinhasssss.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping(value = ["categories"])
@Tag(name = "Categories")
interface CategoryAPI {

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun createCategory(@RequestBody request: CreateCategoryRequest): ResponseEntity<Any>

    @GetMapping(value = ["{id}"], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Category was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun getById(@PathVariable id: String): CategoryResponse

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listCategories(
        @RequestParam(name = "search", required = false, defaultValue = "") search: String,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") perPage: Int,
        @RequestParam(name = "sort", required = false, defaultValue = "name") sort: String,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") direction: String
    ): Pagination<Any>

    @PutMapping(value = ["{id}"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category updated successfully"),
            ApiResponse(responseCode = "404", description = "Category was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun updateById(@PathVariable id: String, @RequestBody request: UpdateCategoryRequest): ResponseEntity<Any>
}
