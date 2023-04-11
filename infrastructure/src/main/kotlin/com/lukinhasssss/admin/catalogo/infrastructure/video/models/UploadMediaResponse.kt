package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType

data class UploadMediaResponse(
    @JsonProperty(value = "video_id") val videoId: String,
    @JsonProperty(value = "media_type") val mediaType: VideoMediaType
)
