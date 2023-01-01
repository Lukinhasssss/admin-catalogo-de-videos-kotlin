package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SpecificationUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class GenrePostgresGateway(
    private val repository: GenreRepository
) : GenreGateway {

    override fun create(aGenre: Genre) = save(aGenre)

    override fun findById(anID: GenreID): Genre? =
        repository.findById(anID.value).map { it.toAggregate() }.orElse(null)

    override fun findAll(aQuery: SearchQuery): Pagination<Genre> = with(aQuery) {
        // Paginação
        val page = PageRequest.of(page, perPage, Sort.by(Sort.Direction.fromString(direction), sort))

        // Busca dinâmica pelo critério terms (name ou description)
        val specifications = Optional.ofNullable(terms)
            .filter { it.isNotBlank() }
            .map { assembleSpecification(it) }.orElse(null)

        val pageResult = repository.findAll(whereClause = Specification.where(specifications), page = page)

        with(pageResult) {
            Pagination(number, size, totalElements, map { it.toAggregate() }.toList())
        }
    }

    override fun update(aGenre: Genre) = save(aGenre)

    override fun deleteById(anID: GenreID) = with(anID.value) {
        if (repository.existsById(this)) repository.deleteById(this)
    }

    override fun existsByIds(genreIDs: Iterable<GenreID>): List<GenreID> {
        throw UnsupportedOperationException()
    }

    private fun assembleSpecification(terms: String) = SpecificationUtils.like<GenreJpaEntity>("name", terms)

    private fun save(aGenre: Genre) = repository.save(GenreJpaEntity.from(aGenre)).toAggregate()
}
