package com.lukinhasssss.admin.catalogo.infrastructure.api.controllers

import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoCommand
import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoUseCase
import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.infrastructure.api.VideoAPI
import com.lukinhasssss.admin.catalogo.infrastructure.utils.HashingUtils
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
class VideoController(
    private val createVideoUseCase: CreateVideoUseCase
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

        val output = createVideoUseCase.execute(aCommand)

        return ResponseEntity.created(
            URI.create("/videos/${output.id}")
        ).body(output)
    }

    override fun createPartial(payload: CreateVideoRequest): ResponseEntity<Any> =
        with(payload) {
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

            return ResponseEntity.created(
                URI.create("/videos/${output.id}")
            ).body(output)
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
