package com.lukinhasssss.admin.catalogo.application.video.media.upload

import com.lukinhasssss.admin.catalogo.domain.video.VideoResource

data class UploadMediaCommand(
    val videoId: String,
    val videoResource: VideoResource
) {

    companion object {
        fun with(videoId: String, aResource: VideoResource) =
            UploadMediaCommand(videoId, aResource)
    }
}
