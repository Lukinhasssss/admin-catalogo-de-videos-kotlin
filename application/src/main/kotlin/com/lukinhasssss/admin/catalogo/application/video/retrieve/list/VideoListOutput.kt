package com.lukinhasssss.admin.catalogo.application.video.retrieve.list

import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoPreview
import java.time.Instant

data class VideoListOutput(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    companion object {
        fun from(aVideo: Video) = with(aVideo) {
            VideoListOutput(
                id = id.value,
                title = title,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        fun from(aVideo: VideoPreview) = with(aVideo) {
            VideoListOutput(
                id = id,
                title = title,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
