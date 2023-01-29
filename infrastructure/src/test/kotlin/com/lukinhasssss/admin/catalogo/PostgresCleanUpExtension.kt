package com.lukinhasssss.admin.catalogo

import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit.jupiter.SpringExtension

class PostgresCleanUpExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val appContext = SpringExtension.getApplicationContext(context)

        cleanUp(
            listOf(
                appContext.getBean(VideoRepository::class.java),
                appContext.getBean(CastMemberRepository::class.java),
                appContext.getBean(GenreRepository::class.java),
                appContext.getBean(CategoryRepository::class.java)
            )
        )
    }

    private fun cleanUp(repositories: List<CrudRepository<out Any, String>>) {
        repositories.forEach { it.deleteAll() }
    }
}
