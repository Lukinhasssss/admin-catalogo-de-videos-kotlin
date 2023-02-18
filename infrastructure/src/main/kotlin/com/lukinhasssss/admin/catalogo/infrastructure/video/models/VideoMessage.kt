package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonProperty

data class VideoMessage(
    @JsonProperty(value = "resource_id")
    val resourceId: String,

    @JsonProperty(value = "file_path")
    val filePath: String
)
