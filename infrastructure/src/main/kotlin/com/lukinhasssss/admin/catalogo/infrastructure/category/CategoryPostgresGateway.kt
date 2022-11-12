package com.lukinhasssss.admin.catalogo.infrastructure.category

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.category.CategorySearchQuery
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryPostgresGateway(
    private val repository: CategoryRepository
) : CategoryGateway {
    override fun create(aCategory: Category): Category {
        return repository.save(CategoryJpaEntity.from(aCategory)).toAggregate()
    }

    override fun findById(anID: CategoryID): Category? {
        TODO("Not yet implemented")
    }

    override fun findAll(aQuery: CategorySearchQuery): Pagination<Category> {
        TODO("Not yet implemented")
    }

    override fun update(aCategory: Category): Category {
        TODO("Not yet implemented")
    }

    override fun deleteById(anID: CategoryID) {
        TODO("Not yet implemented")
    }
}
