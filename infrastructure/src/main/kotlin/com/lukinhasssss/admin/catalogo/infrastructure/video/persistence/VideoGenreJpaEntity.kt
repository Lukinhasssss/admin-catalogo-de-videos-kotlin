package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import java.util.UUID

@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
data class VideoGenreJpaEntity(

    @EmbeddedId
    val id: VideoGenreId,

    @ManyToOne(fetch = LAZY)
    @MapsId(value = "videoId")
    val video: VideoJpaEntity
) {

    companion object {
        fun from(video: VideoJpaEntity, genre: GenreID) = with(video) {
            VideoGenreJpaEntity(
                id = VideoGenreId.from(id, UUID.fromString(genre.value)),
                video = this
            )
        }
    }
}
