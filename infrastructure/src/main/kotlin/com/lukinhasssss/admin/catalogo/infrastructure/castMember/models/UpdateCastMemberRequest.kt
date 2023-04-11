package com.lukinhasssss.admin.catalogo.infrastructure.castMember.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType

data class UpdateCastMemberRequest(

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "type")
    val type: CastMemberType
)
