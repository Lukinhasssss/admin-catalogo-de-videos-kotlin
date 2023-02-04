package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType
import com.lukinhasssss.admin.catalogo.domain.video.VideoResource
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties
import com.lukinhasssss.admin.catalogo.infrastructure.services.StorageService
import org.springframework.stereotype.Component

@Component
class DefaultMediaResourceGateway(
    storageProperties: StorageProperties,
    private val storageService: StorageService
) : MediaResourceGateway {

    private val fileNamePattern = storageProperties.fileNamePattern
    private val locationPattern = storageProperties.locationPattern

    override fun storeAudioVideo(anId: VideoID, videoResource: VideoResource): AudioVideoMedia =
        with(videoResource) {
            val filepath = filepath(anId, this)

            store(filepath, resource)

            AudioVideoMedia.with(
                checksum = resource.checksum,
                name = resource.name,
                rawLocation = filepath
            )
        }

    override fun storeImage(anId: VideoID, imageResource: VideoResource): ImageMedia =
        with(imageResource) {
            val filepath = filepath(anId, this)

            store(filepath, resource)

            ImageMedia.with(
                checksum = resource.checksum,
                name = resource.name,
                location = filepath
            )
        }

    override fun clearResources(anId: VideoID) {
        val ids = storageService.list(folder(anId))
        storageService.deleteAll(ids)
    }

    private fun filepath(anId: VideoID, aResource: VideoResource): String =
        "${folder(anId)}/${filename(aResource.type)}"

    private fun filename(aType: VideoMediaType): String =
        fileNamePattern.replace("{type}", aType.name)

    private fun folder(anId: VideoID): String =
        locationPattern.replace("{videoId}", anId.value)

    private fun store(filepath: String, aResource: Resource) =
        storageService.store(filepath, aResource)
}
