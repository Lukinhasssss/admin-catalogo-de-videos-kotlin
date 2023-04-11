package com.lukinhasssss.admin.catalogo.application.video.media.get

import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType

class DefaultGetMediaUseCase(
    private val mediaResourceGateway: MediaResourceGateway
) : GetMediaUseCase() {

    override fun execute(anIn: GetMediaCommand): MediaOutput = with(anIn) {
        val anId = VideoID.from(videoId)
        val aType = VideoMediaType.of(mediaType)

        val aResource = mediaResourceGateway.getResource(anId, aType) ?: throw notFound(videoId, mediaType)

        return MediaOutput.with(aResource)
    }

    private fun notFound(anId: String, aType: String) =
        NotFoundException.with(
            error = Error(message = "Resource $aType not found for video $anId")
        )
}
