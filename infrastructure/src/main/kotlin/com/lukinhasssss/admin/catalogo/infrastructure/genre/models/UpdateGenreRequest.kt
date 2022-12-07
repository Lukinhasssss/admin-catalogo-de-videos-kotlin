package com.lukinhasssss.admin.catalogo.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateGenreRequest(

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "is_active")
    val active: Boolean,

    @JsonProperty(value = "categories_id")
    val categories: List<String>
)
