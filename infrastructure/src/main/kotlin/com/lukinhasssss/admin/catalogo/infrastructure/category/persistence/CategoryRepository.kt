package com.lukinhasssss.admin.catalogo.infrastructure.category.persistence

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CategoryRepository : ReactiveCrudRepository<CategoryEntity, String>
