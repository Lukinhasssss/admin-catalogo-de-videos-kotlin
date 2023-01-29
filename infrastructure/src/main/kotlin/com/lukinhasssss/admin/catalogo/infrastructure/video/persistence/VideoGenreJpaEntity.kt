package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
class VideoGenreJpaEntity(

    @EmbeddedId
    val id: VideoGenreId,

    @ManyToOne(fetch = LAZY)
    @MapsId(value = "videoId")
    val video: VideoJpaEntity
) {

    constructor(genreId: GenreID, video: VideoJpaEntity) : this(
        id = VideoGenreId.from(genreId = genreId.value, videoId = video.id),
        video = video
    )

    companion object {
        fun from(video: VideoJpaEntity, genreId: GenreID) =
            VideoGenreJpaEntity(genreId = genreId, video = video)
    }
}
