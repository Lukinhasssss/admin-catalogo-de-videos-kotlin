package com.lukinhasssss.admin.catalogo.application.video.media.upload

import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType

data class UploadMediaOutput(
    val videoId: String,
    val mediaType: VideoMediaType
) {

    companion object {
        fun with(aType: VideoMediaType, aVideo: Video) =
            UploadMediaOutput(aVideo.id.value, aType)
    }
}
