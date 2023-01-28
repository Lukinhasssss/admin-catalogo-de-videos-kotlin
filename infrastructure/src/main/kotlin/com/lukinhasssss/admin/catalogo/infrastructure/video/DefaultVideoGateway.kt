package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DefaultVideoGateway(
    private val videoRepository: VideoRepository
) : VideoGateway {

    @Transactional
    override fun create(aVideo: Video): Video = save(aVideo)

    @Transactional(readOnly = true)
    override fun findById(anID: VideoID): Video? {
        return videoRepository.findById(anID.value)
            .map { it.toAggregate() }
            .orElse(null)
    }

    override fun findAll(aQuery: VideoSearchQuery): Pagination<Video> {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun update(aVideo: Video): Video = save(aVideo)

    override fun deleteById(anID: VideoID) = with(anID) {
        if (videoRepository.existsById(value))
            videoRepository.deleteById(value)
    }

    private fun save(aVideo: Video) =
        videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate()
}
