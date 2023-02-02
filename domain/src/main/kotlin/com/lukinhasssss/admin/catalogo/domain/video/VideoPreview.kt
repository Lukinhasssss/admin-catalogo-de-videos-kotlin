package com.lukinhasssss.admin.catalogo.domain.video

import java.time.Instant

data class VideoPreview(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    constructor(aVideo: Video) : this(
        id = aVideo.id.value,
        title = aVideo.title,
        description = aVideo.description,
        createdAt = aVideo.createdAt,
        updatedAt = aVideo.updatedAt
    )
}
