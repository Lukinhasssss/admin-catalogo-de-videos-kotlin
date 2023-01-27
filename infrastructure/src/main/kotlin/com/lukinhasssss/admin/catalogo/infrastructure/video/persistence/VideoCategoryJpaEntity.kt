package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity(name = "VideoCategory")
@Table(name = "videos_categories")
data class VideoCategoryJpaEntity(

    @EmbeddedId
    val id: VideoCategoryId,

    @ManyToOne(fetch = LAZY)
    @MapsId(value = "videoId")
    val video: VideoJpaEntity
) {

    companion object {
        fun from(video: VideoJpaEntity, categoryId: CategoryID) = with(video) {
            VideoCategoryJpaEntity(
                id = VideoCategoryId.from(id, categoryId.value),
                video = this
            )
        }
    }
}
