package com.lukinhasssss.admin.catalogo.infrastructure.configuration.usecases

import com.lukinhasssss.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.delete.DeleteGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.DefaultGetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.DefaultListGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.retrive.list.ListGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase
import com.lukinhasssss.admin.catalogo.application.genre.update.UpdateGenreUseCase
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GenreUseCaseConfig(
    private val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway
) {

    @Bean
    fun createGenreUseCase(): CreateGenreUseCase {
        return DefaultCreateGenreUseCase(
            categoryGateway = categoryGateway,
            genreGateway = genreGateway
        )
    }

    @Bean
    fun updateGenreUseCase(): UpdateGenreUseCase {
        return DefaultUpdateGenreUseCase(
            categoryGateway = categoryGateway,
            genreGateway = genreGateway
        )
    }

    @Bean
    fun getGenreByIdUseCase(): GetGenreByIdUseCase {
        return DefaultGetGenreByIdUseCase(genreGateway = genreGateway)
    }

    @Bean
    fun listGenreUseCase(): ListGenreUseCase {
        return DefaultListGenreUseCase(genreGateway = genreGateway)
    }

    @Bean
    fun deleteGenreUseCase(): DeleteGenreUseCase {
        return DefaultDeleteGenreUseCase(genreGateway = genreGateway)
    }
}
