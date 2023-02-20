package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class VideoListResponse(
    @JsonProperty(value = "id") val id: String,
    @JsonProperty(value = "title") val title: String,
    @JsonProperty(value = "description") val description: String,
    @JsonProperty(value = "created_at") val createdAt: Instant,
    @JsonProperty(value = "updated_at") val updatedAt: Instant
)
