package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoCommand
import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoUseCase
import com.lukinhasssss.admin.catalogo.application.video.delete.DeleteVideoUseCase
import com.lukinhasssss.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase
import com.lukinhasssss.admin.catalogo.application.video.update.UpdateVideoCommand
import com.lukinhasssss.admin.catalogo.application.video.update.UpdateVideoUseCase
import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.infrastructure.api.VideoAPI
import com.lukinhasssss.admin.catalogo.infrastructure.utils.HashingUtils
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.UpdateVideoRequest
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.presenters.toUpdateVideoResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.presenters.toVideoResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
class VideoController(
    private val createVideoUseCase: CreateVideoUseCase,
    private val deleteVideoUseCase: DeleteVideoUseCase,
    private val getVideoByIdUseCase: GetVideoByIdUseCase,
    private val updateVideoUseCase: UpdateVideoUseCase
) : VideoAPI {

    override fun createFull(
        aTitle: String,
        aDescription: String,
        launchedAt: Int,
        aDuration: Double,
        wasOpened: Boolean,
        wasPublished: Boolean,
        aRating: String,
        categories: Set<String>,
        castMembers: Set<String>,
        genres: Set<String>,
        videoFile: MultipartFile?,
        trailerFile: MultipartFile?,
        bannerFile: MultipartFile?,
        thumbFile: MultipartFile?,
        thumbHalfFile: MultipartFile?
    ): ResponseEntity<Any> {
        val aCommand = CreateVideoCommand.with(
            aTitle = aTitle,
            aDescription = aDescription,
            aLaunchYear = launchedAt,
            aDuration = aDuration,
            wasOpened = wasOpened,
            wasPublished = wasPublished,
            aRating = aRating,
            categories = categories,
            genres = genres,
            members = castMembers,
            aVideo = resourceOf(videoFile),
            aTrailer = resourceOf(trailerFile),
            aBanner = resourceOf(bannerFile),
            aThumbnail = resourceOf(thumbFile),
            aThumbnailHalf = resourceOf(thumbHalfFile)
        )

        Logger.info(message = "Iniciando processo de criação de video completo...", payload = aCommand)

        val output = createVideoUseCase.execute(aCommand)

        Logger.info(message = "Finalizado processo de criação de video completo!")
        return ResponseEntity.created(
            URI.create("/videos/${output.id}")
        ).body(output)
    }

    override fun createPartial(payload: CreateVideoRequest): ResponseEntity<Any> {
        with(payload) {
            Logger.info(message = "Iniciando processo de criação de video parcial...", payload = this)

            val aCommand = CreateVideoCommand.with(
                aTitle = title,
                aDescription = description,
                aLaunchYear = launchYear,
                aDuration = duration,
                wasOpened = opened,
                wasPublished = published,
                aRating = rating,
                categories = categories,
                genres = genres,
                members = members
            )

            val output = createVideoUseCase.execute(aCommand)

            Logger.info(message = "Finalizado processo de criação de video parcial!")
            return ResponseEntity.created(
                URI.create("/videos/${output.id}")
            ).body(output)
        }
    }

    override fun getById(id: String): VideoResponse {
        Logger.info(message = "Iniciando processo de busca de video...")

        return getVideoByIdUseCase.execute(id).toVideoResponse().also {
            Logger.info(message = "Finalizado processo de busca de video!", payload = it)
        }
    }

    override fun update(id: String, payload: UpdateVideoRequest): ResponseEntity<Any> {
        with(payload) {
            Logger.info(message = "Iniciando processo de atualização de video...", payload = this)

            val aCommand = UpdateVideoCommand.with(
                anId = id,
                aTitle = title,
                aDescription = description,
                aLaunchYear = launchYear,
                aDuration = duration,
                wasOpened = opened,
                wasPublished = published,
                aRating = rating,
                categories = categories,
                genres = genres,
                members = members
            )

            val output = updateVideoUseCase.execute(aCommand)

            Logger.info(message = "Finalizado processo de atualização de video!")
            return ResponseEntity
                .ok()
                .location(URI.create("/videos/${output.id}"))
                .body(output.toUpdateVideoResponse())
        }
    }

    override fun deleteById(id: String) {
        Logger.info(message = "Iniciando processo de deleção de video...")

        deleteVideoUseCase.execute(id).also {
            Logger.info(message = "Finalizado processo de deleção de video!")
        }
    }

    private fun resourceOf(multipartFile: MultipartFile?) =
        if (multipartFile != null)
            try {
                Resource.with(
                    checksum = HashingUtils.checksum(multipartFile.bytes),
                    content = multipartFile.bytes,
                    contentType = multipartFile.contentType!!,
                    name = multipartFile.originalFilename!!
                )
            } catch (t: Throwable) {
                throw RuntimeException(t)
            }
        else null
}
