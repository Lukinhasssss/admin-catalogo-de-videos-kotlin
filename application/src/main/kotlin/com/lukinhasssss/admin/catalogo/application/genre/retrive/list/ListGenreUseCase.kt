package com.lukinhasssss.admin.catalogo.application.genre.retrive.list

import com.lukinhasssss.admin.catalogo.application.UseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

abstract class ListGenreUseCase : UseCase<SearchQuery, Pagination<GenreListOutput>>()
