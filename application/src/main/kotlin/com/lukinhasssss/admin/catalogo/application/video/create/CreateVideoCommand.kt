package com.lukinhasssss.admin.catalogo.application.video.create

import com.lukinhasssss.admin.catalogo.domain.video.Resource

data class CreateVideoCommand(
    val title: String,
    val description: String,
    val launchedAt: Int,
    val duration: Double,
    val opened: Boolean,
    val published: Boolean,
    val rating: String,
    val categories: Set<String>,
    val genres: Set<String>,
    val members: Set<String>,
    val video: Resource?,
    val trailer: Resource?,
    val banner: Resource?,
    val thumbnail: Resource?,
    val thumbnailHalf: Resource?
) {
    companion object {
        fun with(
            aTitle: String,
            aDescription: String,
            aLaunchYear: Int,
            aDuration: Double,
            wasOpened: Boolean,
            wasPublished: Boolean,
            aRating: String,
            categories: Set<String>,
            genres: Set<String>,
            members: Set<String>,
            aVideo: Resource? = null,
            aTrailer: Resource? = null,
            aBanner: Resource? = null,
            aThumbnail: Resource? = null,
            aThumbnailHalf: Resource? = null
        ) = CreateVideoCommand(
            aTitle, aDescription, aLaunchYear, aDuration, wasOpened,
            wasPublished, aRating, categories, genres, members,
            aVideo, aTrailer, aBanner, aThumbnail, aThumbnailHalf
        )
    }
}
