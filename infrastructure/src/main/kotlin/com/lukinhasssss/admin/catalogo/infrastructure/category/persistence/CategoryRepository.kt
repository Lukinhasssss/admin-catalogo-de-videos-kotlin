package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CategoryRepository : JpaRepository<CategoryJpaEntity, String> {

    fun findAll(
        whereClause: Specification<CategoryJpaEntity>,
        page: Pageable
    ): Page<CategoryJpaEntity>

    @Query(value = "select category.id from Category category where category.id in :ids")
    fun existsByIds(@Param("ids") ids: List<String>): List<String>
}
