package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.Objects

@Entity(name = "VideoCategory")
@Table(name = "videos_categories")
class VideoCategoryJpaEntity(

    @EmbeddedId
    val id: VideoCategoryId,

    @ManyToOne(fetch = LAZY)
    @MapsId(value = "videoId")
    val video: VideoJpaEntity
) {

    constructor(categoryId: CategoryID, video: VideoJpaEntity) : this(
        id = VideoCategoryId.from(categoryId = categoryId.value, videoId = video.id),
        video = video
    )

    companion object {
        fun from(video: VideoJpaEntity, categoryId: CategoryID) =
            VideoCategoryJpaEntity(categoryId = categoryId, video = video)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) {
            return false
        }

        other as VideoCategoryJpaEntity

        return id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id)
}
