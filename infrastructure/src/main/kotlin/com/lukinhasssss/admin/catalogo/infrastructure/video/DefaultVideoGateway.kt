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
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DefaultVideoGateway(
    private val videoRepository: VideoRepository
) : VideoGateway {

    @Transactional
    override fun create(aVideo: Video): Video {
        Logger.info(message = "Iniciando inserção do video no banco...")

        return save(aVideo).also { Logger.info(message = "Video inserido no banco com sucesso!") }
    }

    @Transactional(readOnly = true)
    override fun findById(anID: VideoID): Video? {
        Logger.info(message = "Iniciando busca do video no banco...")

        return videoRepository.findById(anID.value)
            .map { it.toAggregate() }
            .orElse(null)
            .also { Logger.info(message = "Finalizada busca do video no banco!") }
    }

    override fun findAll(aQuery: VideoSearchQuery): Pagination<VideoPreview> = with(aQuery) {
        val page = PageRequest.of(page, perPage, Sort.by(Sort.Direction.fromString(direction), sort))

        Logger.info(message = "Iniciando busca paginada dos videos no banco...")

        val actualPage = videoRepository.findAll(
            terms = SqlUtils.like(term = terms),
            castMembers = castMembers.mapTo(Identifier::value),
            categories = categories.mapTo(Identifier::value),
            genres = genres.mapTo(Identifier::value),
            page = page
        )

        Logger.info(message = "Finalizada busca paginada dos videos no banco!")

        Pagination(
            currentPage = actualPage.number,
            perPage = actualPage.size,
            total = actualPage.totalElements,
            items = actualPage.toList()
        )
    }

    @Transactional
    override fun update(aVideo: Video): Video {
        Logger.info(message = "Iniciando atualização do video no banco...")

        return save(aVideo).also { Logger.info(message = "Video atualizado no banco com sucesso!") }
    }

    override fun deleteById(anID: VideoID) = with(anID) {
        Logger.info(message = "Iniciando deleção do video salvo no banco...")

        if (videoRepository.existsById(value)) videoRepository.deleteById(value).also {
            Logger.info(message = "Video deletado do banco com sucesso!")
        }
    }

    private fun save(aVideo: Video): Video =
        videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate()
}
