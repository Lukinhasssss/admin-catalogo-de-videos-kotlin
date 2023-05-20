package com.lukinhasssss.admin.catalogo.application.video.media.update

import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus

data class UpdateMediaStatusCommand(
    val videoId: String,
    val status: MediaStatus,
    val resourceId: String,
    val folder: String,
    val filename: String
) {

    companion object {
        fun with(
            videoId: String,
            status: MediaStatus,
            resourceId: String,
            folder: String,
            filename: String
        ) = UpdateMediaStatusCommand(
            videoId,
            status,
            resourceId,
            folder,
            filename
        )
    }
}
