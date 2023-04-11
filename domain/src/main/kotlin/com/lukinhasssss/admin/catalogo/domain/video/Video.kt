package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.event.DomainEvent
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import java.time.Instant
import java.time.Year

data class Video(
    override val id: VideoID,
    val title: String,
    val description: String,
    val launchedAt: Year,
    val duration: Double,
    val rating: Rating,

    val opened: Boolean,
    val published: Boolean,

    val createdAt: Instant,
    var updatedAt: Instant,

    var banner: ImageMedia? = null,
    var thumbnail: ImageMedia? = null,
    var thumbnailHalf: ImageMedia? = null,

    var trailer: AudioVideoMedia? = null,
    var video: AudioVideoMedia? = null,

    val categories: Set<CategoryID>,
    val genres: Set<GenreID>,
    val castMembers: Set<CastMemberID>,

    override val domainEvents: MutableList<DomainEvent> = mutableListOf()
) : AggregateRoot<VideoID>(id, domainEvents) {

    override fun validate(handler: ValidationHandler) =
        VideoValidator(aVideo = this, validationHandler = handler).validate()

    companion object {
        fun newVideo(
            aTitle: String,
            aDescription: String,
            aLaunchYear: Year,
            aDuration: Double,
            wasOpened: Boolean,
            wasPublished: Boolean,
            aRating: Rating,
            categories: Set<CategoryID> = emptySet(),
            genres: Set<GenreID> = emptySet(),
            members: Set<CastMemberID> = emptySet()
        ): Video {
            val anId = VideoID.unique()
            val now = InstantUtils.now()

            return Video(
                id = anId,
                title = aTitle,
                description = aDescription,
                launchedAt = aLaunchYear,
                duration = aDuration,
                opened = wasOpened,
                published = wasPublished,
                rating = aRating,
                createdAt = now,
                updatedAt = now,
                categories = categories,
                genres = genres,
                castMembers = members
            )
        }

        fun with(
            anId: VideoID,
            aTitle: String,
            aDescription: String,
            aLaunchYear: Year,
            aDuration: Double,
            aRating: Rating,
            wasOpened: Boolean,
            wasPublished: Boolean,
            aCreationDate: Instant,
            anUpdateDate: Instant,
            aBanner: ImageMedia? = null,
            aThumbnail: ImageMedia? = null,
            aThumbnailHalf: ImageMedia? = null,
            aTrailer: AudioVideoMedia? = null,
            aVideo: AudioVideoMedia? = null,
            categories: Set<CategoryID> = emptySet(),
            genres: Set<GenreID> = emptySet(),
            members: Set<CastMemberID> = emptySet()
        ) = Video(
            id = anId,
            title = aTitle,
            description = aDescription,
            launchedAt = aLaunchYear,
            duration = aDuration,
            rating = aRating,
            opened = wasOpened,
            published = wasPublished,
            createdAt = aCreationDate,
            updatedAt = anUpdateDate,
            banner = aBanner,
            thumbnail = aThumbnail,
            thumbnailHalf = aThumbnailHalf,
            trailer = aTrailer,
            video = aVideo,
            categories = categories,
            genres = genres,
            castMembers = members
        )
    }

    fun update(
        aTitle: String,
        aDescription: String,
        aLaunchYear: Year,
        aDuration: Double,
        wasOpened: Boolean,
        wasPublished: Boolean,
        aRating: Rating,
        categories: Set<CategoryID>,
        genres: Set<GenreID>,
        members: Set<CastMemberID>
    ) = Video(
        id = id,
        title = aTitle,
        description = aDescription,
        launchedAt = aLaunchYear,
        duration = aDuration,
        opened = wasOpened,
        published = wasPublished,
        rating = aRating,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        categories = categories,
        genres = genres,
        castMembers = members,
        domainEvents = domainEvents
    )

    fun updateVideoMedia(aVideoMedia: AudioVideoMedia) = Video(
        id = id,
        title = title,
        description = description,
        launchedAt = launchedAt,
        duration = duration,
        opened = opened,
        published = published,
        rating = rating,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        categories = categories,
        genres = genres,
        castMembers = castMembers,
        video = aVideoMedia,
        trailer = trailer,
        banner = banner,
        thumbnail = thumbnail,
        thumbnailHalf = thumbnailHalf,
        domainEvents = domainEvents
    )

    fun updateTrailerMedia(aTrailerMedia: AudioVideoMedia) = Video(
        id = id,
        title = title,
        description = description,
        launchedAt = launchedAt,
        duration = duration,
        opened = opened,
        published = published,
        rating = rating,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        categories = categories,
        genres = genres,
        castMembers = castMembers,
        video = video,
        trailer = aTrailerMedia,
        banner = banner,
        thumbnail = thumbnail,
        thumbnailHalf = thumbnailHalf,
        domainEvents = domainEvents
    )

    fun updateBannerMedia(aBannerMedia: ImageMedia) = Video(
        id = id,
        title = title,
        description = description,
        launchedAt = launchedAt,
        duration = duration,
        opened = opened,
        published = published,
        rating = rating,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        categories = categories,
        genres = genres,
        castMembers = castMembers,
        video = video,
        trailer = trailer,
        banner = aBannerMedia,
        thumbnail = thumbnail,
        thumbnailHalf = thumbnailHalf,
        domainEvents = domainEvents
    )

    fun updateThumbnailMedia(aThumbMedia: ImageMedia) = Video(
        id = id,
        title = title,
        description = description,
        launchedAt = launchedAt,
        duration = duration,
        opened = opened,
        published = published,
        rating = rating,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        categories = categories,
        genres = genres,
        castMembers = castMembers,
        video = video,
        trailer = trailer,
        banner = banner,
        thumbnail = aThumbMedia,
        thumbnailHalf = thumbnailHalf,
        domainEvents = domainEvents
    )

    fun updateThumbnailHalfMedia(aThumbMedia: ImageMedia) = Video(
        id = id,
        title = title,
        description = description,
        launchedAt = launchedAt,
        duration = duration,
        opened = opened,
        published = published,
        rating = rating,
        createdAt = createdAt,
        updatedAt = InstantUtils.now(),
        categories = categories,
        genres = genres,
        castMembers = castMembers,
        video = video,
        trailer = trailer,
        banner = banner,
        thumbnail = thumbnail,
        thumbnailHalf = aThumbMedia
    )

    fun processing(aMediaType: VideoMediaType): Video {
        if (aMediaType == VIDEO && video != null)
            return updateVideoMedia(video!!.processing())

        if (aMediaType == TRAILER && trailer != null)
            return updateTrailerMedia(trailer!!.processing())

        return this
    }

    fun completed(aMediaType: VideoMediaType, encodedPath: String): Video {
        if (aMediaType == VIDEO && video != null)
            return updateVideoMedia(video!!.completed(encodedPath))

        if (aMediaType == TRAILER && trailer != null)
            return updateTrailerMedia(trailer!!.completed(encodedPath))

        return this
    }
}
