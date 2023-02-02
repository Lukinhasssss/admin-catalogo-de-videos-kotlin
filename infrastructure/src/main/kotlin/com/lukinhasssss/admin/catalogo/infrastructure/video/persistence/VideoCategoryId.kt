package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.Objects

@Embeddable
class VideoCategoryId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "category_id", nullable = false)
    val categoryId: String
) : Serializable {

    companion object {
        private const val serialVersionUID = -104L

        fun from(videoId: String, categoryId: String) =
            VideoCategoryId(videoId, categoryId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other))
            return false

        other as VideoCategoryId

        return videoId == other.videoId &&
            categoryId == other.categoryId
    }

    override fun hashCode(): Int = Objects.hash(videoId, categoryId)
}
