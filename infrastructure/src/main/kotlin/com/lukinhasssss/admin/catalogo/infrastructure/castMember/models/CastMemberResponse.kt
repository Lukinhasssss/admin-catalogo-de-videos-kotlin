package com.lukinhasssss.admin.catalogo.infrastructure.castMember.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CastMemberResponse(

    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "type")
    val type: String,

    @JsonProperty(value = "created_at")
    val createdAt: String,

    @JsonProperty(value = "updated_at")
    val updatedAt: String
)
