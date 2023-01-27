package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import java.util.UUID

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
data class VideoCastMemberJpaEntity(

    @EmbeddedId
    val id: VideoCastMemberId,

    @ManyToOne(fetch = LAZY)
    @MapsId(value = "videoId")
    val video: VideoJpaEntity
) {

    companion object {
        fun from(video: VideoJpaEntity, castMemberId: CastMemberID) = with(video) {
            VideoCastMemberJpaEntity(
                id = VideoCastMemberId.from(id, UUID.fromString(castMemberId.value)),
                video = this
            )
        }
    }
}
