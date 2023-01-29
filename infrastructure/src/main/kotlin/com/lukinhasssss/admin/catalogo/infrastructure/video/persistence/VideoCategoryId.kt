package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class VideoCategoryId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "category_id", nullable = false)
    val categoryId: String
) : Serializable {

    companion object {
        fun from(videoId: String, categoryId: String) =
            VideoCategoryId(videoId, categoryId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VideoCategoryId

        if (videoId != other.videoId) return false
        if (categoryId != other.categoryId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = videoId.hashCode()
        result = 31 * result + categoryId.hashCode()
        return result
    }
}
