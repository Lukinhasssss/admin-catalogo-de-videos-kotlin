package com.lukinhasssss.admin.catalogo.application.video.retrieve.get

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.utils.CollectionUtils.mapTo
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.Rating
import com.lukinhasssss.admin.catalogo.domain.video.Video
import java.time.Instant

data class VideoOutput(
    val id: String,
    val title: String,
    val description: String,
    val launchedAt: Int,
    val duration: Double,
    val opened: Boolean,
    val published: Boolean,
    val rating: Rating,
    val categories: Set<String>,
    val genres: Set<String>,
    val castMembers: Set<String>,
    val video: AudioVideoMedia? = null,
    val trailer: AudioVideoMedia? = null,
    val banner: ImageMedia? = null,
    val thumbnail: ImageMedia? = null,
    val thumbnailHalf: ImageMedia? = null,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    companion object {
        fun from(aVideo: Video) = with(aVideo) {
            VideoOutput(
                id = id.value,
                title = title,
                description = description,
                launchedAt = launchedAt.value,
                duration = duration,
                opened = opened,
                published = published,
                rating = rating,
                categories = categories.mapTo(Identifier::value),
                genres = genres.mapTo(Identifier::value),
                castMembers = castMembers.mapTo(Identifier::value),
                video = video,
                trailer = trailer,
                banner = banner,
                thumbnail = thumbnail,
                thumbnailHalf = thumbnailHalf,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
