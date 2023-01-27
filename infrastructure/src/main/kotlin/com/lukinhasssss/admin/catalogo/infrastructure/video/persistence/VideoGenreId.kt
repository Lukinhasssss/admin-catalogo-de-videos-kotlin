package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class VideoGenreId(

    @Column(name = "video_id", nullable = false)
    val videoId: UUID,

    @Column(name = "genre_id", nullable = false)
    val genreId: UUID
) : Serializable {

    companion object {
        fun from(videoId: UUID, genreId: UUID) =
            VideoGenreId(videoId, genreId)
    }
}
