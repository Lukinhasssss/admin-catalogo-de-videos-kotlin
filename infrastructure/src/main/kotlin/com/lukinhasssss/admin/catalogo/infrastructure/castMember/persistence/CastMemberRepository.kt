package com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CastMemberRepository : JpaRepository<CastMemberJpaEntity, String> {

    fun findAll(
        specification: Specification<CastMemberJpaEntity>,
        page: Pageable
    ): Page<CastMemberJpaEntity>

    @Query(value = "select castMember.id from CastMember castMember where castMember.id in :ids")
    fun existsByIds(@Param("ids") ids: List<String>): List<String>
}
