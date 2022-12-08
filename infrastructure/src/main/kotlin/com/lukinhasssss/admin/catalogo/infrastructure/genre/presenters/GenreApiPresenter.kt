package com.lukinhasssss.admin.catalogo.infrastructure.genre.presenters

import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GenreOutput
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.GenreListOutput
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.lukinhasssss.admin.catalogo.infrastructure.genre.models.GenreResponse

fun GenreOutput.toGenreResponse() =
    GenreResponse(
        id = id,
        name = name,
        active = isActive,
        categories = categories,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )

fun GenreListOutput.toGenreListResponse() =
    GenreListResponse(
        id = id,
        name = name,
        active = isActive,
        createdAt = createdAt,
        deletedAt = deletedAt
    )
