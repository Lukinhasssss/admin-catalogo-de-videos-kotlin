package com.lukinhasssss.admin.catalogo.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class GenreResponse(

    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "is_active")
    val active: Boolean,

    @JsonProperty(value = "categories_id")
    val categories: List<String>,

    @JsonProperty(value = "created_at")
    val createdAt: Instant,

    @JsonProperty(value = "updated_at")
    val updatedAt: Instant,

    @JsonProperty(value = "deleted_at")
    val deletedAt: Instant?
)
