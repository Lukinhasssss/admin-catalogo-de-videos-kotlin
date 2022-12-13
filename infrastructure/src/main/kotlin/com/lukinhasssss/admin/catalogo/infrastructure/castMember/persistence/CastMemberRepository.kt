package com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface CastMemberRepository : JpaRepository<CastMemberJpaEntity, String> {

    fun findAll(
        specification: Specification<CastMemberJpaEntity>,
        page: Pageable
    ): Page<CastMemberJpaEntity>
}
