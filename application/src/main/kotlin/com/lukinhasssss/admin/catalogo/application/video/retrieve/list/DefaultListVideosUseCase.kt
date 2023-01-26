package com.lukinhasssss.admin.catalogo.application.video.retrieve.list

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery

class DefaultListVideosUseCase(
    private val videoGateway: VideoGateway
) : ListVideosUseCase() {

    override fun execute(anIn: VideoSearchQuery): Pagination<VideoListOutput> =
        videoGateway.findAll(anIn).map(VideoListOutput::from)
}
