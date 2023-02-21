package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty

data class AudioVideoMediaResponse(
    @JsonProperty(value = "id") val id: String,
    @JsonProperty(value = "checksum") val checksum: String,
    @JsonProperty(value = "name") val name: String,
    @JsonProperty(value = "location") val rawLocation: String,
    @JsonProperty(value = "encoded_location") val encodedLocation: String,
    @JsonProperty(value = "status") val status: String
)
