package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID

data class VideoSearchQuery(
    val page: Int,
    val perPage: Int,
    val terms: String,
    val sort: String,
    val direction: String,
    val castMembers: Set<CastMemberID> = emptySet(),
    val categories: Set<CategoryID> = emptySet(),
    val genres: Set<GenreID> = emptySet()
)
