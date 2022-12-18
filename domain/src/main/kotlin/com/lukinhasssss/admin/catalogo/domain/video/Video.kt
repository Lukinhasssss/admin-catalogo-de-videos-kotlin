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

    val banner: ImageMedia? = null,
    val thumbnail: ImageMedia? = null,
    val thumbnailHalf: ImageMedia? = null,

    val trailer: AudioVideoMedia? = null,
    val video: AudioVideoMedia? = null,

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
}
