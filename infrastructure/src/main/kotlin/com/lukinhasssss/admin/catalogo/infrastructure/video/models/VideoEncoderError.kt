package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName(value = "ERROR")
data class VideoEncoderError(
    @JsonProperty(value = "message")
    val message: VideoMessage,

    @JsonProperty(value = "error")
    val error: String
) : VideoEncoderResult {

    companion object {
        private const val ERROR = "ERROR"
    }

    override fun getStatus() = ERROR
}
