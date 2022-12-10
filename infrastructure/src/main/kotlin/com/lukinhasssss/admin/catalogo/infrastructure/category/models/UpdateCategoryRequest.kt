package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateCategoryRequest(

    @JsonProperty(value = "name")
    val name: String = "",

    @JsonProperty(value = "description")
    val description: String?,

    @JsonProperty(value = "is_active")
    val active: Boolean?
) {
    fun isActive() = active ?: true
}
