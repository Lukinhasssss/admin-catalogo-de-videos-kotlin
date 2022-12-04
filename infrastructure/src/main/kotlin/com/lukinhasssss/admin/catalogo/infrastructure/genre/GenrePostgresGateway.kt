package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.springframework.stereotype.Component

@Component
class GenrePostgresGateway(
    private val genreRepository: GenreRepository
) : GenreGateway {

    override fun create(aGenre: Genre): Genre {
        return save(aGenre)
    }

    override fun findById(anID: GenreID): Genre? {
        TODO("Not yet implemented")
    }

    override fun findAll(aQuery: SearchQuery): Pagination<Genre> {
        TODO("Not yet implemented")
    }

    override fun update(aGenre: Genre): Genre {
        return save(aGenre)
    }

    override fun deleteById(anID: GenreID) {
        TODO("Not yet implemented")
    }

    private fun save(aGenre: Genre) =
        genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate()
}
