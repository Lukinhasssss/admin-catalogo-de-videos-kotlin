package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.video.Rating
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.Year
import java.util.UUID

@Table(name = "videos")
@Entity(name = "Video")
data class VideoJpaEntity(

    @Id
    val id: UUID,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", length = 4000)
    val description: String?,

    @Column(name = "year_launched", nullable = false)
    val yearLaunched: Int,

    @Column(name = "opened", nullable = false)
    val opened: Boolean,

    @Column(name = "published", nullable = false)
    val published: Boolean,

    @Column(name = "rating", nullable = false)
    @Enumerated(value = STRING)
    val rating: Rating,

    @Column(name = "duration", precision = 2, nullable = false)
    val duration: Double,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val updatedAt: Instant
) {

    companion object {
        fun from(aVideo: Video) = with(aVideo) {
            VideoJpaEntity(
                id = UUID.fromString(id.value),
                title = title,
                description = description,
                yearLaunched = launchedAt.value,
                opened = opened,
                published = published,
                rating = rating,
                duration = duration,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    fun toAggregate() = Video.with(
        anId = VideoID.from(id),
        aTitle = title,
        aDescription = description,
        aLaunchYear = Year.of(yearLaunched),
        aDuration = duration,
        aRating = rating,
        wasOpened = opened,
        wasPublished = published,
        aCreationDate = createdAt,
        anUpdateDate = updatedAt
    )
}
