package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.Objects

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) {
            return false
        }

        other as VideoGenreJpaEntity

        return id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id)
}
