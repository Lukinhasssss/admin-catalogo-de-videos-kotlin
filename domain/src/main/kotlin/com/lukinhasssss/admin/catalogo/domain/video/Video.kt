package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import java.time.Instant
import java.time.Year

data class Video(
    override val id: VideoID,
    val title: String,
    val description: String? = null,
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
    val castMembers: Set<CastMemberID>
) : AggregateRoot<VideoID>(id) {

    override fun validate(handler: ValidationHandler) =
        VideoValidator(aVideo = this, validationHandler = handler).validate()

    companion object {
        fun newVideo(
            aTitle: String,
            aDescription: String?,
            aLaunchYear: Year,
            aDuration: Double,
            wasOpened: Boolean,
            wasPublished: Boolean,
            aRating: Rating,
            categories: Set<CategoryID>,
            genres: Set<GenreID>,
            members: Set<CastMemberID>
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
    }

    fun update(
        aTitle: String,
        aDescription: String?,
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
        castMembers = members
    )

    fun setVideo(aVideoMedia: AudioVideoMedia) = Video(
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
        thumbnailHalf = thumbnailHalf
    )

    fun setTrailer(aTrailerMedia: AudioVideoMedia) = Video(
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
        thumbnailHalf = thumbnailHalf
    )

    fun setBanner(aBannerMedia: ImageMedia) = Video(
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
        thumbnailHalf = thumbnailHalf
    )

    fun setThumbnail(aThumbMedia: ImageMedia) = Video(
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
        thumbnailHalf = thumbnailHalf
    )

    fun setThumbnailHalf(aThumbMedia: ImageMedia) = Video(
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
}
