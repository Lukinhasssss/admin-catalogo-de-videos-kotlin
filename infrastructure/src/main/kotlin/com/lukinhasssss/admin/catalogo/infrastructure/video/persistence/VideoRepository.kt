package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VideoRepository : JpaRepository<VideoJpaEntity, UUID>
