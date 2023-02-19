package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateVideoRequest(
    @JsonProperty(value = "title") val title: String,
    @JsonProperty(value = "description") val description: String,
    @JsonProperty(value = "year_launched") val launchYear: Int,
    @JsonProperty(value = "duration") val duration: Double,
    @JsonProperty(value = "opened") val opened: Boolean,
    @JsonProperty(value = "published") val published: Boolean,
    @JsonProperty(value = "rating") val rating: String,
    @JsonProperty(value = "categories") val categories: Set<String> = emptySet(),
    @JsonProperty(value = "genres") val genres: Set<String> = emptySet(),
    @JsonProperty(value = "cast_members") val members: Set<String> = emptySet()
)
