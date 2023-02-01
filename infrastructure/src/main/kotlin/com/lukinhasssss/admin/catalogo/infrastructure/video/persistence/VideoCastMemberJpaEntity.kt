package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.Objects

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
class VideoCastMemberJpaEntity(

    @EmbeddedId
    val id: VideoCastMemberId,

    @ManyToOne(fetch = LAZY)
    @MapsId(value = "videoId")
    val video: VideoJpaEntity
) {

    constructor(castMemberId: CastMemberID, video: VideoJpaEntity) : this(
        id = VideoCastMemberId.from(castMemberId = castMemberId.value, videoId = video.id),
        video = video
    )

    companion object {
        fun from(video: VideoJpaEntity, castMemberId: CastMemberID) =
            VideoCastMemberJpaEntity(castMemberId = castMemberId, video = video)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other))
            return false

        other as VideoCastMemberJpaEntity

        return id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id)
}
