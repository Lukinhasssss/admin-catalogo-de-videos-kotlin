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
    private val repository: GenreRepository
) : GenreGateway {

    override fun create(aGenre: Genre): Genre {
        return save(aGenre)
    }

    override fun findById(anID: GenreID): Genre? {
        return repository.findById(anID.value)
            .map { it.toAggregate() }.orElse(null)
    }

    override fun findAll(aQuery: SearchQuery): Pagination<Genre> {
        TODO("Not yet implemented")
    }

    override fun update(aGenre: Genre): Genre {
        return save(aGenre)
    }

    override fun deleteById(anID: GenreID) {
        with(anID.value) {
            if (repository.existsById(this)) repository.deleteById(this)
        }
    }

    private fun save(aGenre: Genre) =
        repository.save(GenreJpaEntity.from(aGenre)).toAggregate()
}
