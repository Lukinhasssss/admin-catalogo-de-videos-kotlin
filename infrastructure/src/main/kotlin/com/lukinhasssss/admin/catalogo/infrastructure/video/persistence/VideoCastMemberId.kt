package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.Objects

@Embeddable
class VideoCastMemberId(

    @Column(name = "video_id", nullable = false)
    val videoId: String,

    @Column(name = "cast_member_id", nullable = false)
    val castMemberId: String
) : Serializable {

    companion object {
        private const val serialVersionUID = -60L

        fun from(videoId: String, castMemberId: String) =
            VideoCastMemberId(videoId, castMemberId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other))
            return false

        other as VideoCastMemberId

        return videoId == other.videoId &&
            castMemberId == other.castMemberId
    }

    override fun hashCode(): Int = Objects.hash(videoId, castMemberId)
}
