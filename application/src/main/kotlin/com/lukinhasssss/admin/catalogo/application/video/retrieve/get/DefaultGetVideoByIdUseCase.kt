package com.lukinhasssss.admin.catalogo.application.video.retrieve.get

import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID

class DefaultGetVideoByIdUseCase(
    private val videoGateway: VideoGateway
) : GetVideoByIdUseCase() {

    override fun execute(anIn: String): VideoOutput {
        val anId = VideoID.from(anIn)

        return videoGateway.findById(anId)?.map()
            ?: throw notFound(anId)
    }

    private fun Video.map() = VideoOutput.from(this)

    private fun notFound(anVideoID: VideoID) =
        NotFoundException.with(
            id = anVideoID,
            anAggregate = Video::class
        )
}
