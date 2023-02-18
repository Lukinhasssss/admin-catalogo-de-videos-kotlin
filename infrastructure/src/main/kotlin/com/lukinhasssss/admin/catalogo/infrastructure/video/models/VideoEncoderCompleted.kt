package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName(value = "COMPLETED")
data class VideoEncoderCompleted(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "output_bucket_path")
    val outputBucket: String,

    @JsonProperty(value = "video")
    val video: VideoMetadata
) : VideoEncoderResult {

    companion object {
        private const val COMPLETED = "COMPLETED"
    }

    override fun getStatus() = COMPLETED
}
