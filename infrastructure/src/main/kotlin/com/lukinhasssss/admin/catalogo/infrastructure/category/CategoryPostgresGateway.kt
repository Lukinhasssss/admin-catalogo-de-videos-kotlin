package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.category.CategorySearchQuery
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.lukinhasssss.admin.catalogo.infrastructure.utils.SpecificationUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CategoryPostgresGateway(
    private val repository: CategoryRepository
) : CategoryGateway {
    override fun create(aCategory: Category): Category {
        return save(aCategory)
    }

    override fun findById(anID: CategoryID): Category? {
        return repository.findById(anID.value)
            .map { it.toAggregate() }.orElse(null)
    }

    override fun findAll(aQuery: CategorySearchQuery): Pagination<Category> = with(aQuery) {
        // Paginação
        val page = PageRequest.of(page, perPage, Sort.by(Direction.fromString(direction), sort))

        // Busca dinâmica pelo critério terms (name ou description)
        val specifications = Optional.ofNullable(terms)
            .filter { it.isNotBlank() }
            .map {
                val nameLike: Specification<CategoryJpaEntity> = SpecificationUtils.like("name", it)
                val descriptionLike: Specification<CategoryJpaEntity> = SpecificationUtils.like("description", it)
                nameLike.or(descriptionLike)
            }.orElse(null)

        val pageResult = repository.findAll(Specification.where(specifications), page)

        with(pageResult) {
            Pagination(number, size, totalElements, map { it.toAggregate() }.toList())
        }
    }

    override fun update(aCategory: Category): Category {
        return save(aCategory)
    }

    override fun deleteById(anID: CategoryID) {
        with(anID.value) {
            if (repository.existsById(this)) repository.deleteById(this)
        }
    }

    private fun save(aCategory: Category): Category {
        return repository.save(CategoryJpaEntity.from(aCategory)).toAggregate()
    }
}
