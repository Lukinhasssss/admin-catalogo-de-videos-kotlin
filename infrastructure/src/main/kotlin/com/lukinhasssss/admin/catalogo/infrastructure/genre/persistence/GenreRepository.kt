package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GenreRepository : JpaRepository<GenreJpaEntity, String> {

    fun findAll(
        whereClause: Specification<GenreJpaEntity>,
        page: Pageable
    ): Page<GenreJpaEntity>

    @Query(value = "select genre.id from Genre genre where genre.id in :ids")
    fun existsByIds(@Param("ids") ids: List<String>): List<String>
}
