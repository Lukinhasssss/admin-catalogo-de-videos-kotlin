package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryJpaEntity, String> {

    fun findAll(
        whereClause: Specification<CategoryJpaEntity>,
        page: Pageable
    ): Page<CategoryJpaEntity>
}
