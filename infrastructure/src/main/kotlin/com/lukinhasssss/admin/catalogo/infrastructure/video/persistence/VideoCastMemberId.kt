package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class VideoCastMemberId(

    @Column(name = "video_id", nullable = false)
    val videoId: UUID,

    @Column(name = "cast_member_id", nullable = false)
    val castMemberId: UUID
) : Serializable {

    companion object {
        fun from(videoId: UUID, castMemberId: UUID) =
            VideoCastMemberId(videoId, castMemberId)
    }
}
