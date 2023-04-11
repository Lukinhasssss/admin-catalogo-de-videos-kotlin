package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty

data class VideoMetadata(
    @JsonProperty(value = "encoded_video_folder")
    val encodedVideoFolder: String,

    @JsonProperty(value = "resource_id")
    val resourceId: String,

    @JsonProperty(value = "file_path")
    val filePath: String
)
