package com.lukinhasssss.admin.catalogo.application.video.delete

import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID

class DefaultDeleteVideoUseCase(
    private val videoGateway: VideoGateway,
    private val mediaResourceGateway: MediaResourceGateway
) : DeleteVideoUseCase() {

    override fun execute(anIn: String) {
        val aVideoId = VideoID.from(anIn)

        videoGateway.deleteById(aVideoId)
        mediaResourceGateway.clearResources(aVideoId)
    }
}
