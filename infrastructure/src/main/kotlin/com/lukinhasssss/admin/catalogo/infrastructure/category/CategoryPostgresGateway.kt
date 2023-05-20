package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SpecificationUtils
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class CategoryPostgresGateway(
    private val repository: CategoryRepository
) : CategoryGateway {

    override fun create(aCategory: Category): Category {
        return save(aCategory)
    }

    override fun findById(anID: CategoryID): Category? {
        Logger.info(message = "Iniciando busca da categoria no banco...")

        return repository.findById(anID.value)
            .map { it.toAggregate() }
            .orElse(null)
            .also { Logger.info(message = "Finalizada busca da categoria no banco!") }
    }

    override fun findAll(aQuery: SearchQuery): Pagination<Category> = with(aQuery) {
        // Paginação
        val page = PageRequest.of(page, perPage, Sort.by(Direction.fromString(direction), sort))

        // Busca dinâmica pelo critério terms (name ou description)
        val specifications = Optional.ofNullable(terms)
            .filter { it.isNotBlank() }
            .map { assembleSpecification(it) }.orElse(null)

        Logger.info(message = "Iniciando busca paginada das categorias no banco...")

        val pageResult = repository.findAll(Specification.where(specifications), page)

        Logger.info(message = "Finalizada busca paginada das categorias no banco!")

        with(pageResult) {
            Pagination(number, size, totalElements, map { it.toAggregate() }.toList())
        }
    }

    override fun update(aCategory: Category): Category {
        return save(aCategory)
    }

    override fun deleteById(anID: CategoryID) {
        with(anID.value) {
            Logger.info(message = "Iniciando deleção da categoria salva no banco...")

            if (repository.existsById(this)) {
                repository.deleteById(this).also {
                    Logger.info(message = "Categoria deletada do banco com sucesso!")
                }
            }
        }
    }

    override fun existsByIds(categoryIDs: Iterable<CategoryID>): List<CategoryID> {
        Logger.info(message = "Verificando se a(s) categoria(s) existe(m) no banco...")

        val ids = categoryIDs.map { it.value }

        return repository.existsByIds(ids).map { CategoryID.from(it) }.also {
            Logger.info(message = "Finalizada verificação se a(s) categoria(s) existe(m) no banco!")
        }
    }

    private fun save(aCategory: Category): Category {
        Logger.info(message = "Iniciando inserção da categoria no banco...")

        return repository.save(CategoryJpaEntity.from(aCategory)).toAggregate().also {
            Logger.info(message = "Categoria inserida no banco com sucesso!")
        }
    }

    private fun assembleSpecification(str: String): Specification<CategoryJpaEntity> {
        val nameLike: Specification<CategoryJpaEntity> = SpecificationUtils.like("name", str)
        val descriptionLike: Specification<CategoryJpaEntity> = SpecificationUtils.like("description", str)
        return nameLike.or(descriptionLike)
    }
}
