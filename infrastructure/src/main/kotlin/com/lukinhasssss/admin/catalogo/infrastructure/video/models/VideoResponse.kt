package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class VideoResponse(
    @JsonProperty(value = "id") val id: String,
    @JsonProperty(value = "title") val title: String,
    @JsonProperty(value = "description") val description: String,
    @JsonProperty(value = "year_launched") val launchYear: Int,
    @JsonProperty(value = "duration") val duration: Double,
    @JsonProperty(value = "opened") val opened: Boolean,
    @JsonProperty(value = "published") val published: Boolean,
    @JsonProperty(value = "rating") val rating: String,
    @JsonProperty(value = "created_at") val createdAt: Instant,
    @JsonProperty(value = "updated_at") val updatedAt: Instant,
    @JsonProperty(value = "video") val video: AudioVideoMediaResponse?,
    @JsonProperty(value = "trailer") val trailer: AudioVideoMediaResponse?,
    @JsonProperty(value = "banner") val banner: ImageMediaResponse?,
    @JsonProperty(value = "thumbnail") val thumbnail: ImageMediaResponse?,
    @JsonProperty(value = "thumbnail_half") val thumbnailHalf: ImageMediaResponse?,
    @JsonProperty(value = "categories_id") val categories: Set<String> = emptySet(),
    @JsonProperty(value = "genres_id") val genres: Set<String> = emptySet(),
    @JsonProperty(value = "cast_members_id") val members: Set<String> = emptySet()
)
