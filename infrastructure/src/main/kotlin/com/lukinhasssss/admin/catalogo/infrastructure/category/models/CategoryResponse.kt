package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class CategoryResponse(

    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "description")
    val description: String?,

    @JsonProperty(value = "is_active")
    val active: Boolean,

    @JsonProperty(value = "created_at")
    val createdAt: Instant,

    @JsonProperty(value = "updated_at")
    val updatedAt: Instant,

    @JsonProperty(value = "deleted_at")
    val deletedAt: Instant?
)
