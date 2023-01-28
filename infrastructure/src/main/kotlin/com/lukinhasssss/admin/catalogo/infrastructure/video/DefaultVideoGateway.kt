package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import jakarta.transaction.Transactional

class DefaultVideoGateway(
    private val videoRepository: VideoRepository
) : VideoGateway {

    @Transactional
    override fun create(aVideo: Video): Video {
        return videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate()
    }

    override fun findById(anID: VideoID): Video? {
        TODO("Not yet implemented")
    }

    override fun findAll(aQuery: VideoSearchQuery): Pagination<Video> {
        TODO("Not yet implemented")
    }

    override fun update(aVideo: Video): Video {
        TODO("Not yet implemented")
    }

    override fun deleteById(anID: VideoID) = with(anID) {
        if (videoRepository.existsById(value))
            videoRepository.deleteById(value)
    }
}
