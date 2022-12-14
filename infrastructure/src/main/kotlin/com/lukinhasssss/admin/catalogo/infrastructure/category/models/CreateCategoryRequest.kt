package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateCategoryRequest(

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "description")
    val description: String?,

    @JsonProperty(value = "is_active")
    val active: Boolean? = null
) {

    fun isActive() = active ?: true
}
