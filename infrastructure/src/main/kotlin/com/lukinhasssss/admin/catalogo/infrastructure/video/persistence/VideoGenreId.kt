package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class VideoGenreId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "genre_id", nullable = false)
    val genreId: String
) : Serializable {

    companion object {
        fun from(videoId: String, genreId: String) =
            VideoGenreId(videoId, genreId)
    }
}
