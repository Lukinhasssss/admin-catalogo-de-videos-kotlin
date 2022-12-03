package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import org.springframework.stereotype.Component

@Component
class GenrePostgresGateway : GenreGateway {

    override fun create(aGenre: Genre): Genre {
        TODO("Not yet implemented")
    }

    override fun findById(anID: GenreID): Genre? {
        TODO("Not yet implemented")
    }

    override fun findAll(aQuery: SearchQuery): Pagination<Genre> {
        TODO("Not yet implemented")
    }

    override fun update(aGenre: Genre): Genre {
        TODO("Not yet implemented")
    }

    override fun deleteById(anID: GenreID) {
        TODO("Not yet implemented")
    }
}
