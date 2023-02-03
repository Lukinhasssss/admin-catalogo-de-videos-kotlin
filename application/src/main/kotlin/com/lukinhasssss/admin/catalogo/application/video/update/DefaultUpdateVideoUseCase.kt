package com.lukinhasssss.admin.catalogo.application.video.update

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.InternalErrorException
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.Rating
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL_HALF
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import com.lukinhasssss.admin.catalogo.domain.video.VideoResource
import java.time.Year

class DefaultUpdateVideoUseCase(
    private val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway,
    private val castMemberGateway: CastMemberGateway,
    private val mediaResourceGateway: MediaResourceGateway,
    private val videoGateway: VideoGateway
) : UpdateVideoUseCase() {

    override fun execute(anIn: UpdateVideoCommand) = with(anIn) {
        val anId = VideoID.from(id)
        val aLaunchYear = Year.of(launchedAt)
        val aRating = Rating.of(rating)

        val categories = toIdentifier(categories, CategoryID::from)
        val genres = toIdentifier(genres, GenreID::from)
        val members = toIdentifier(members, CastMemberID::from)

        val aVideo = videoGateway.findById(anId) ?: throw notFound(anId)

        val notification = Notification.create()

        notification.append(validateCategories(categories))
        notification.append(validateGenres(genres))
        notification.append(validateMembers(members))

        val aVideoUpdated = aVideo.update(
            aTitle = title,
            aDescription = description,
            aLaunchYear = aLaunchYear,
            aDuration = duration,
            wasOpened = opened,
            wasPublished = published,
            aRating = aRating,
            categories = categories,
            genres = genres,
            members = members
        )

        aVideoUpdated.validate(notification)

        if (notification.hasError())
            throw NotificationException(
                message = "Could not update Aggregate Video",
                notification = notification
            )

        UpdateVideoOutput.from(update(aCommand = this, aVideo = aVideoUpdated))
    }

    private fun notFound(anId: VideoID) =
        NotFoundException.with(anId, Video::class)

    private fun update(aCommand: UpdateVideoCommand, aVideo: Video): Video = with(aCommand) {
        val anId = aVideo.id

        try {
            val aVideoMedia = if (video != null) {
                mediaResourceGateway.storeAudioVideo(
                    anId = anId, aResource = VideoResource.with(video, VIDEO)
                )
            } else null

            val aTrailerMedia = if (trailer != null) {
                mediaResourceGateway.storeAudioVideo(
                    anId = anId, aResource = VideoResource.with(trailer, TRAILER)
                )
            } else null

            val aBannerMedia = if (banner != null) {
                mediaResourceGateway.storeImage(
                    anId = anId, aResource = VideoResource.with(banner, BANNER)
                )
            } else null

            val aThumbnailMedia = if (thumbnail != null) {
                mediaResourceGateway.storeImage(
                    anId = anId, aResource = VideoResource.with(thumbnail, THUMBNAIL)
                )
            } else null

            val aThumbnailHalfMedia = if (thumbnailHalf != null) {
                mediaResourceGateway.storeImage(
                    anId = anId, aResource = VideoResource.with(thumbnailHalf, THUMBNAIL_HALF)
                )
            } else null

            videoGateway.update(
                aVideo.copy(
                    video = aVideoMedia,
                    trailer = aTrailerMedia,
                    banner = aBannerMedia,
                    thumbnail = aThumbnailMedia,
                    thumbnailHalf = aThumbnailHalfMedia
                )
            )
        } catch (throwable: Throwable) {
            throw InternalErrorException.with(
                message = "An error on update video was observed [videoID: $anId]",
                throwable = throwable
            )
        }
    }

    private fun validateCategories(ids: Set<CategoryID>): ValidationHandler =
        validateAggregate(aggregate = "categories", ids = ids, categoryGateway::existsByIds)

    private fun validateGenres(ids: Set<GenreID>): ValidationHandler =
        validateAggregate(aggregate = "genres", ids = ids, genreGateway::existsByIds)

    private fun validateMembers(ids: Set<CastMemberID>): ValidationHandler =
        validateAggregate(aggregate = "cast members", ids = ids, castMemberGateway::existsByIds)

    private fun <T : Identifier> validateAggregate(
        aggregate: String,
        ids: Set<T>,
        existsByIds: (Iterable<T>) -> List<T>
    ): ValidationHandler {
        val notification = Notification.create()

        if (ids.isEmpty()) return notification

        val retrievedIds = existsByIds.invoke(ids)

        if (ids.size != retrievedIds.size) {
            val missingIds = ids.toMutableList()
            missingIds.removeAll(retrievedIds)

            val missingIdsMessage = missingIds.joinToString(separator = ", ") { it.value }

            notification.append(Error("Some $aggregate could not be found: $missingIdsMessage"))
        }

        return notification
    }

    private fun <T> toIdentifier(ids: Set<String>, mapper: (String) -> T): Set<T> {
        return ids.stream().map(mapper).toList().toSet()
    }
}
