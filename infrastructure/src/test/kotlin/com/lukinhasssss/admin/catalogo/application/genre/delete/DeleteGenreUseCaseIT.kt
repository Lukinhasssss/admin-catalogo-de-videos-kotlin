package com.lukinhasssss.admin.catalogo.application.genre.delete

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class DeleteGenreUseCaseIT {

    @Autowired
    private lateinit var useCase: DefaultDeleteGenreUseCase

    @Autowired
    private lateinit var genreGateway: GenreGateway

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        val aGenre = genreGateway.create(Genre.newGenre("Ação", true))

        val expectedId = aGenre.id

        assertEquals(1, genreRepository.count())

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        // then
        assertEquals(0, genreRepository.count())
    }

    @Test
    fun givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        // given
        genreGateway.create(Genre.newGenre("Ação", true))

        val expectedId = GenreID.from("any")

        assertEquals(1, genreRepository.count())

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        // then
        assertEquals(1, genreRepository.count())
    }
}
