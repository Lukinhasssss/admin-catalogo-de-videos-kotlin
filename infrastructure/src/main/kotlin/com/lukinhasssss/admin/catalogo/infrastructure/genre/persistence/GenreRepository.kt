package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository : JpaRepository<GenreJpaEntity, String> {

    fun findAll(
        whereClause: Specification<GenreJpaEntity>,
        page: Pageable
    ): Page<GenreJpaEntity>
}
