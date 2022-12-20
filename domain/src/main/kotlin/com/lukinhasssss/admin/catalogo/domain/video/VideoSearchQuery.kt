package com.lukinhasssss.admin.catalogo.domain.video

data class VideoSearchQuery(
    val page: Int,
    val perPage: Int,
    val terms: String,
    val sort: String,
    val direction: String
)
