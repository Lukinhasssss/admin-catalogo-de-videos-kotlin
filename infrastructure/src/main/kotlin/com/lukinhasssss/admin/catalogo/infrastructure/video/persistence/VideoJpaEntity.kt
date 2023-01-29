package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.video.Rating
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.time.Year

@Table(name = "videos")
@Entity(name = "Video")
data class VideoJpaEntity(

    @Id
    val id: String,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", length = 4000)
    val description: String,

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
    val updatedAt: Instant,

    @OneToOne(cascade = [ALL], fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    val trailer: AudioVideoMediaJpaEntity?,

    @OneToOne(cascade = [ALL], fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    val video: AudioVideoMediaJpaEntity?,

    @OneToOne(cascade = [ALL], fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_id")
    val banner: ImageMediaJpaEntity?,

    @OneToOne(cascade = [ALL], fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    val thumbnail: ImageMediaJpaEntity?,

    @OneToOne(cascade = [ALL], fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_half_id")
    val thumbnailHalf: ImageMediaJpaEntity?,

    @OneToMany(mappedBy = "video", cascade = [ALL], orphanRemoval = true)
    val categories: MutableSet<VideoCategoryJpaEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "video", cascade = [ALL], orphanRemoval = true)
    val genres: MutableSet<VideoGenreJpaEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "video", cascade = [ALL], orphanRemoval = true)
    val castMembers: MutableSet<VideoCastMemberJpaEntity> = mutableSetOf()
) {

    companion object {
        fun from(aVideo: Video) = with(aVideo) {
            val entity = VideoJpaEntity(
                id = id.value,
                title = title,
                description = description,
                yearLaunched = launchedAt.value,
                opened = opened,
                published = published,
                rating = rating,
                duration = duration,
                createdAt = createdAt,
                updatedAt = updatedAt,
                trailer = AudioVideoMediaJpaEntity.from(trailer),
                video = AudioVideoMediaJpaEntity.from(video),
                banner = ImageMediaJpaEntity.from(banner),
                thumbnail = ImageMediaJpaEntity.from(thumbnail),
                thumbnailHalf = ImageMediaJpaEntity.from(thumbnailHalf)
            )

            categories.forEach { entity.addCategory(it) }
            genres.forEach { entity.addGenre(it) }
            castMembers.forEach { entity.addCastMember(it) }

            entity
        }
    }

    fun addCategory(anId: CategoryID) =
        categories.add(VideoCategoryJpaEntity.from(this, anId))

    fun addGenre(anId: GenreID) =
        genres.add(VideoGenreJpaEntity.from(this, anId))

    fun addCastMember(anId: CastMemberID) =
        castMembers.add(VideoCastMemberJpaEntity.from(this, anId))

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
        anUpdateDate = updatedAt,
        aTrailer = trailer?.toDomain(),
        aVideo = video?.toDomain(),
        aBanner = banner?.toDomain(),
        aThumbnail = thumbnail?.toDomain(),
        aThumbnailHalf = thumbnailHalf?.toDomain(),
        categories = categories.map { CategoryID.from(it.id.categoryId) }.toSet(),
        genres = genres.map { GenreID.from(it.id.genreId) }.toSet(),
        members = castMembers.map { CastMemberID.from(it.id.castMemberId) }.toSet()
    )
}
