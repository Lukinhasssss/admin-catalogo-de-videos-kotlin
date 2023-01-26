package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SpecificationUtils
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
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

    override fun findById(anID: GenreID): Genre? {
        Logger.info(message = "Iniciando busca do gênero no banco...")

        return repository.findById(anID.value)
            .map { it.toAggregate() }
            .orElse(null)
            .also { Logger.info(message = "Finalizada busca do gênero no banco!") }
    }

    override fun findAll(aQuery: SearchQuery): Pagination<Genre> = with(aQuery) {
        // Paginação
        val page = PageRequest.of(page, perPage, Sort.by(Sort.Direction.fromString(direction), sort))

        // Busca dinâmica pelo critério terms (name ou description)
        val specifications = Optional.ofNullable(terms)
            .filter { it.isNotBlank() }
            .map { assembleSpecification(it) }.orElse(null)

        Logger.info(message = "Iniciando busca paginada dos gêneros no banco...")

        val pageResult = repository.findAll(whereClause = Specification.where(specifications), page = page)

        Logger.info(message = "Finalizada busca paginada dos gêneros no banco!")

        with(pageResult) {
            Pagination(number, size, totalElements, map { it.toAggregate() }.toList())
        }
    }

    override fun update(aGenre: Genre) = save(aGenre)

    override fun deleteById(anID: GenreID) = with(anID.value) {
        Logger.info(message = "Iniciando deleção do gênero salvo no banco...")

        if (repository.existsById(this)) repository.deleteById(this).also {
            Logger.info(message = "Gênero deletado do banco com sucesso!")
        }
    }

    private fun save(aGenre: Genre): Genre {
        Logger.info(message = "Iniciando inserção do gênero no banco...")

        return repository.save(GenreJpaEntity.from(aGenre)).toAggregate().also {
            Logger.info(message = "Gênero inserido no banco com sucesso!")
        }
    }

    override fun existsByIds(genreIDs: Iterable<GenreID>): List<GenreID> {
        throw UnsupportedOperationException()
    }

    private fun assembleSpecification(terms: String) = SpecificationUtils.like<GenreJpaEntity>("name", terms)
}
