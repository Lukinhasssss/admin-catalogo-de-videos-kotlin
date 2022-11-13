package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class CategoryResponse(

    @field:JsonProperty(value = "id")
    val id: String,

    @field:JsonProperty(value = "name")
    val name: String,

    @field:JsonProperty(value = "description")
    val description: String?,

    @field:JsonProperty(value = "is_active")
    val active: Boolean,

    @field:JsonProperty(value = "created_at")
    val createdAt: Instant,

    @field:JsonProperty(value = "updated_at")
    val updatedAt: Instant,

    @field:JsonProperty(value = "deleted_at")
    val deletedAt: Instant?
)
