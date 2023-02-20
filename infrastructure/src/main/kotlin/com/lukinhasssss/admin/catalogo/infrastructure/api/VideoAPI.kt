package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.UpdateVideoRequest
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.MultipartFile

@RequestMapping(value = ["videos"])
@Tag(name = "Video")
interface VideoAPI {

    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new video with medias")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun createFull(
        @RequestParam(name = "title", required = false) aTitle: String,
        @RequestParam(name = "description", required = false) aDescription: String,
        @RequestParam(name = "year_launched", required = false) launchedAt: Int,
        @RequestParam(name = "duration", required = false) aDuration: Double,
        @RequestParam(name = "opened", required = false) wasOpened: Boolean,
        @RequestParam(name = "published", required = false) wasPublished: Boolean,
        @RequestParam(name = "rating", required = false) aRating: String,
        @RequestParam(name = "categories_id", required = false) categories: Set<String>,
        @RequestParam(name = "cast_members_id", required = false) castMembers: Set<String>,
        @RequestParam(name = "genres_id", required = false) genres: Set<String>,
        @RequestParam(name = "video_file", required = false) videoFile: MultipartFile?,
        @RequestParam(name = "trailer_file", required = false) trailerFile: MultipartFile?,
        @RequestParam(name = "banner_file", required = false) bannerFile: MultipartFile?,
        @RequestParam(name = "thumb_file", required = false) thumbFile: MultipartFile?,
        @RequestParam(name = "thumb_half_file", required = false) thumbHalfFile: MultipartFile?
    ): ResponseEntity<Any>

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new video without medias")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun createPartial(@RequestBody payload: CreateVideoRequest): ResponseEntity<Any>

    @GetMapping(value = ["{id}"], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a video by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Video was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun getById(@PathVariable(name = "id") id: String): VideoResponse

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "List all videos paginated")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Videos listed"),
            ApiResponse(responseCode = "422", description = "A query param was invalid"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun list(
        @RequestParam(name = "search", required = false, defaultValue = "") search: String,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "perPage", required = false, defaultValue = "25") perPage: Int,
        @RequestParam(name = "sort", required = false, defaultValue = "title") sort: String,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") direction: String,
        @RequestParam(name = "cast_members_ids", required = false, defaultValue = "") castMembers: Set<String>,
        @RequestParam(name = "categories_ids", required = false, defaultValue = "") categories: Set<String>,
        @RequestParam(name = "genres_ids", required = false, defaultValue = "") genres: Set<String>
    ): Pagination<VideoListResponse>

    @PutMapping(value = ["{id}"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Update a video by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Video was not found"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun update(
        @PathVariable(name = "id") id: String,
        @RequestBody payload: UpdateVideoRequest
    ): ResponseEntity<Any>

    @GetMapping(value = ["{id}/medias/{type}"])
    @Operation(summary = "Get a video media by it's type")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Media retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Media was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun getMediaByType(
        @PathVariable(name = "id") id: String,
        @PathVariable(name = "type") type: String
    ): ResponseEntity<ByteArray>

    @DeleteMapping(value = ["{id}"])
    @ResponseStatus(value = NO_CONTENT)
    @Operation(summary = "Delete a video by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Video deleted"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun deleteById(@PathVariable(name = "id") id: String)
}
