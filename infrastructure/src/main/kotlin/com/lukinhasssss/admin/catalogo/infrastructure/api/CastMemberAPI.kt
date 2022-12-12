package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CastMemberResponse
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.UpdateCastMemberRequest
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

@RequestMapping(value = ["cast_members"])
@Tag(name = "Cast Members")
interface CastMemberAPI {

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new cast member")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun create(@RequestBody request: CreateCastMemberRequest): ResponseEntity<Any>

    @GetMapping(value = ["{id}"], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a cast member by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cast member retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Cast member was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun getById(@PathVariable(name = "id") id: String): CastMemberResponse

    @PutMapping(value = ["{id}"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Update a cast member by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cast member updated successfully"),
            ApiResponse(responseCode = "404", description = "Cast member was not found"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun updateById(
        @PathVariable(name = "id") id: String,
        @RequestBody request: UpdateCastMemberRequest
    ): ResponseEntity<Any>
}
