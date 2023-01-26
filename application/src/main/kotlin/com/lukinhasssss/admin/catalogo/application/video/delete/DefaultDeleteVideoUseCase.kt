package com.lukinhasssss.admin.catalogo.application.video.delete

import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID

class DefaultDeleteVideoUseCase(
    private val videoGateway: VideoGateway
) : DeleteVideoUseCase() {

    override fun execute(anIn: String) = videoGateway.deleteById(VideoID.from(anIn))
}
