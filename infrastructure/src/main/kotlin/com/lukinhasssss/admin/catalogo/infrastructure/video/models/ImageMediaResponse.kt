package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageMediaResponse(
    @JsonProperty(value = "id") val id: String,
    @JsonProperty(value = "checksum") val checksum: String,
    @JsonProperty(value = "name") val name: String,
    @JsonProperty(value = "location") val location: String
)
