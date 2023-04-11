package com.lukinhasssss.admin.catalogo.application.video.media.update

import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.COMPLETED
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PENDING
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PROCESSING
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO

class DefaultUpdateMediaStatusUseCase(
    private val videoGateway: VideoGateway
) : UpdateMediaStatusUseCase() {

    override fun execute(anIn: UpdateMediaStatusCommand) = with(anIn) {
        val anId = VideoID.from(videoId)

        val aVideo = videoGateway.findById(anId) ?: throw notFound(anId)

        val encodedPath = "$folder/$filename"

        if (matches(resourceId, aVideo.video)) {
            updateVideo(VIDEO, status, aVideo, encodedPath)
        }

        if (matches(resourceId, aVideo.trailer)) {
            updateVideo(TRAILER, status, aVideo, encodedPath)
        }
    }

    private fun updateVideo(
        aMediaType: VideoMediaType,
        aStatus: MediaStatus,
        aVideo: Video,
        encodedPath: String
    ) {
        when (aStatus) {
            PENDING -> Unit
            PROCESSING -> videoGateway.update(aVideo.processing(aMediaType = aMediaType))
            COMPLETED -> videoGateway.update(aVideo.completed(aMediaType = aMediaType, encodedPath = encodedPath))
        }
    }

    private fun matches(anId: String, aMedia: AudioVideoMedia?): Boolean {
        if (aMedia != null) {
            return aMedia.id == anId
        }

        return false
    }

    private fun notFound(anId: VideoID) =
        NotFoundException.with(
            id = anId,
            anAggregate = Video::class
        )
}
