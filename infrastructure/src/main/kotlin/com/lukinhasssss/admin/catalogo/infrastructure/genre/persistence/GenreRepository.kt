package com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository : JpaRepository<GenreJpaEntity, String>
