package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class VideoCastMemberId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "cast_member_id", nullable = false)
    val castMemberId: String
) : Serializable {

    companion object {
        fun from(videoId: String, castMemberId: String) =
            VideoCastMemberId(videoId, castMemberId)
    }
}
