package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class VideoCategoryId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "category_id", nullable = false)
    val categoryId: String
) : Serializable {

    companion object {
        fun from(videoId: String, categoryId: String) =
            VideoCategoryId(videoId, categoryId)
    }
}
