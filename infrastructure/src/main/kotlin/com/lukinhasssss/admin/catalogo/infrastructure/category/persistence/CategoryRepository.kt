package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryJpaEntity, String>
