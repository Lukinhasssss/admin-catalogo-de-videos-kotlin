package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class VideoCategoryId(

    @Column(name = "video_id", nullable = false)
    val videoId: UUID,

    @Column(name = "category_id", nullable = false)
    val categoryId: UUID
) : Serializable {

    companion object {
        fun from(videoId: UUID, categoryId: UUID) =
            VideoCategoryId(videoId, categoryId)
    }
}
