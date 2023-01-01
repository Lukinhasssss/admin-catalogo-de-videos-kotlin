package com.lukinhasssss.admin.catalogo.infrastructure.castMember

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SpecificationUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class CastMemberPostgresGateway(
    private val repository: CastMemberRepository
) : CastMemberGateway {

    override fun create(aCastMember: CastMember): CastMember = save(aCastMember)

    override fun findById(anID: CastMemberID): CastMember? =
        repository.findById(anID.value).map { it.toAggregate() }.orElse(null)

    override fun findAll(aQuery: SearchQuery): Pagination<CastMember> = with(aQuery) {
        // Paginação
        val page = PageRequest.of(page, perPage, Sort.by(Sort.Direction.fromString(direction), sort))

        // Busca dinâmica pelo critério terms (name ou description)
        val specifications = Optional.ofNullable(terms)
            .filter { it.isNotBlank() }
            .map { assembleSpecification(it) }.orElse(null)

        val pageResult = repository.findAll(specification = Specification.where(specifications), page = page)

        with(pageResult) {
            Pagination(number, size, totalElements, map { it.toAggregate() }.toList())
        }
    }

    override fun update(aCastMember: CastMember): CastMember = save(aCastMember)

    override fun deleteById(anID: CastMemberID) = with(anID.value) {
        if (repository.existsById(this)) repository.deleteById(this)
    }

    override fun existsByIds(castMemberIDs: Iterable<CastMemberID>): List<CastMemberID> {
        throw UnsupportedOperationException()
    }

    private fun assembleSpecification(term: String) = SpecificationUtils.like<CastMemberJpaEntity>("name", term)

    private fun save(aMember: CastMember) = repository.save(CastMemberJpaEntity.from(aMember)).toAggregate()
}
