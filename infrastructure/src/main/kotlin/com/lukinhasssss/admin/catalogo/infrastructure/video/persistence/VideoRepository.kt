package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository : JpaRepository<VideoJpaEntity, String>
