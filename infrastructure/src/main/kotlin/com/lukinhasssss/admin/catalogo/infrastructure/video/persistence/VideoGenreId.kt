package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.Objects

@Embeddable
class VideoGenreId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "genre_id", nullable = false)
    val genreId: String
) : Serializable {

    companion object {
        private const val serialVersionUID = -9762081793943244L

        fun from(videoId: String, genreId: String) =
            VideoGenreId(videoId, genreId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as VideoGenreId

        return videoId == other.videoId &&
            genreId == other.genreId
    }

    override fun hashCode(): Int = Objects.hash(videoId, genreId)
}
