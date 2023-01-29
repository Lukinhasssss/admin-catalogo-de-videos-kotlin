package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.utils.CollectionUtils.mapTo
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoPreview
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SqlUtils
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

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

    override fun findAll(aQuery: VideoSearchQuery): Pagination<VideoPreview> = with(aQuery) {
        val page = PageRequest.of(page, perPage, Sort.by(Sort.Direction.fromString(direction), sort))

        val actualPage = videoRepository.findAll(
            terms = SqlUtils.like(term = terms),
            castMembers = castMembers.mapTo(Identifier::value),
            categories = categories.mapTo(Identifier::value),
            genres = genres.mapTo(Identifier::value),
            page = page
        )

        Pagination(
            currentPage = actualPage.number,
            perPage = actualPage.size,
            total = actualPage.totalElements,
            items = actualPage.toList()
        )
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
