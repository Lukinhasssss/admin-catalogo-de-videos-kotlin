package com.lukinhasssss.admin.catalogo.infrastructure.video.presenters

import com.lukinhasssss.admin.catalogo.application.video.media.upload.UploadMediaOutput
import com.lukinhasssss.admin.catalogo.application.video.retrieve.get.VideoOutput
import com.lukinhasssss.admin.catalogo.application.video.retrieve.list.VideoListOutput
import com.lukinhasssss.admin.catalogo.application.video.update.UpdateVideoOutput
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.AudioVideoMediaResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.ImageMediaResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.UpdateVideoResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.UploadMediaResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoResponse

fun VideoOutput.toResponse() =
    VideoResponse(
        id = id,
        title = title,
        description = description,
        launchYear = launchedAt,
        duration = duration,
        opened = opened,
        published = published,
        rating = rating.description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        video = video?.toResponse(),
        trailer = trailer?.toResponse(),
        banner = banner?.toResponse(),
        thumbnail = thumbnail?.toResponse(),
        thumbnailHalf = thumbnailHalf?.toResponse(),
        categories = categories,
        genres = genres,
        members = castMembers
    )

private fun AudioVideoMedia.toResponse() =
    AudioVideoMediaResponse(
        id = id,
        checksum = checksum,
        name = name,
        rawLocation = rawLocation,
        encodedLocation = encodedLocation,
        status = status.name
    )

private fun ImageMedia.toResponse() =
    ImageMediaResponse(
        id = id,
        checksum = checksum,
        name = name,
        location = location
    )

fun Pagination<VideoListOutput>.toResponse() =
    map { it.toResponse() }

private fun VideoListOutput.toResponse() =
    VideoListResponse(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UpdateVideoOutput.toResponse() = UpdateVideoResponse(id = id)

fun UploadMediaOutput.toResponse() = UploadMediaResponse(videoId = videoId, mediaType = mediaType)
