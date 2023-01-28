package com.lukinhasssss.admin.catalogo.domain.video

import java.time.Instant

data class VideoPreview(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
