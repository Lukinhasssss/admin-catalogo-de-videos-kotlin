package com.lukinhasssss.admin.catalogo.infrastructure.castMember

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SpecificationUtils
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
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

    override fun findById(anID: CastMemberID): CastMember? {
        Logger.info(message = "Iniciando busca do membro de elenco no banco...")

        return repository.findById(anID.value)
            .map { it.toAggregate() }
            .orElse(null)
            .also { Logger.info(message = "Finalizada busca do membro de elenco no banco") }
    }

    override fun findAll(aQuery: SearchQuery): Pagination<CastMember> = with(aQuery) {
        // Paginação
        val page = PageRequest.of(page, perPage, Sort.by(Sort.Direction.fromString(direction), sort))

        // Busca dinâmica pelo critério terms (name ou description)
        val specifications = Optional.ofNullable(terms)
            .filter { it.isNotBlank() }
            .map { assembleSpecification(it) }.orElse(null)

        Logger.info(message = "Iniciando busca paginada dos membros de elenco no banco...")

        val pageResult = repository.findAll(specification = Specification.where(specifications), page = page)

        Logger.info(message = "Finalizada busca paginada dos membros de elenco no banco!")

        with(pageResult) {
            Pagination(number, size, totalElements, map { it.toAggregate() }.toList())
        }
    }

    override fun update(aCastMember: CastMember): CastMember = save(aCastMember)

    override fun deleteById(anID: CastMemberID) = with(anID.value) {
        Logger.info(message = "Iniciando deleção do membro de elenco salvo no banco...")

        if (repository.existsById(this)) repository.deleteById(this).also {
            Logger.info(message = "Membro de elenco deletado do banco com sucesso!")
        }
    }

    private fun save(aMember: CastMember): CastMember {
        Logger.info(message = "Iniciando inserção do membro de elenco no banco...")

        return repository.save(CastMemberJpaEntity.from(aMember)).toAggregate().also {
            Logger.info(message = "Finalizada inserção do membro de elenco no banco!")
        }
    }

    override fun existsByIds(castMemberIDs: Iterable<CastMemberID>): List<CastMemberID> {
        throw UnsupportedOperationException()
    }

    private fun assembleSpecification(term: String) = SpecificationUtils.like<CastMemberJpaEntity>("name", term)
}
