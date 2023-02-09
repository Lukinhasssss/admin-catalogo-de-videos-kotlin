package com.lukinhasssss.admin.catalogo.application.video.media.upload

import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL_HALF
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO

class DefaultUploadMediaUseCase(
    private val videoGateway: VideoGateway,
    private val mediaResourceGateway: MediaResourceGateway
) : UploadMediaUseCase() {

    override fun execute(anIn: UploadMediaCommand): UploadMediaOutput = with(anIn) {
        val anId = VideoID.from(videoId)

        var aVideo = videoGateway.findById(anId) ?: throw notFound(anId)

        aVideo = when (videoResource.type) {
            VIDEO -> aVideo.setVideo(mediaResourceGateway.storeAudioVideo(anId, videoResource))
            TRAILER -> aVideo.setTrailer(mediaResourceGateway.storeAudioVideo(anId, videoResource))
            BANNER -> aVideo.setBanner(mediaResourceGateway.storeImage(anId, videoResource))
            THUMBNAIL -> aVideo.setThumbnail(mediaResourceGateway.storeImage(anId, videoResource))
            THUMBNAIL_HALF -> aVideo.setThumbnailHalf(mediaResourceGateway.storeImage(anId, videoResource))
        }

        UploadMediaOutput.with(
            aVideo = videoGateway.update(aVideo),
            aType = videoResource.type
        )
    }

    private fun notFound(anId: VideoID) =
        NotFoundException.with(id = anId, anAggregate = Video::class)
}
