package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

interface GenreGateway {

    fun create(aGenre: Genre): Genre

    fun findById(anID: GenreID): Genre?

    fun findAll(aQuery: SearchQuery): Pagination<Genre>

    fun update(aGenre: Genre): Genre

    fun deleteById(anID: GenreID)
}
