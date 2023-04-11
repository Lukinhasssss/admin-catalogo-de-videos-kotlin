package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateVideoResponse(
    @JsonProperty(value = "id") val id: String
)
