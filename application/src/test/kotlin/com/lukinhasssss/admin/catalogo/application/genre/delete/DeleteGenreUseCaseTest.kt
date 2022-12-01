package com.lukinhasssss.admin.catalogo.application.genre.delete

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DeleteGenreUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultDeleteGenreUseCase

    @MockK
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        val aGenre = Genre.newGenre("Ação", true)

        val expectedId = aGenre.id

        every { genreGateway.deleteById(any()) } returns Unit

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        verify(exactly = 1) { genreGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        // given
        val expectedId = GenreID.from("any")

        every { genreGateway.deleteById(any()) } returns Unit

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        verify(exactly = 1) { genreGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        // given
        val aGenre = Genre.newGenre("Ação", true)

        val expectedId = aGenre.id

        every { genreGateway.deleteById(any()) } throws IllegalStateException("Gateway error")

        // when
        assertThrows<IllegalStateException> { useCase.execute(expectedId.value) }

        verify(exactly = 1) { genreGateway.deleteById(expectedId) }
    }
}
