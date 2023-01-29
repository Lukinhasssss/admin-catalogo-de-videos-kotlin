package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class VideoGenreId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "genre_id", nullable = false)
    val genreId: String
) {

    companion object {
        fun from(videoId: String, genreId: String) =
            VideoGenreId(videoId, genreId)
    }
}
