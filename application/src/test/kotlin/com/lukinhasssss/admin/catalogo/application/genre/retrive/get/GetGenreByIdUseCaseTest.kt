package com.lukinhasssss.admin.catalogo.application.genre.retrive.get

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetGenreByIdUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultGetGenreByIdUseCase

    @MockK
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )

        val aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories)

        val expectedId = aGenre.id

        every { genreGateway.findById(any()) } returns aGenre

        // when
        val actualGenre = useCase.execute(expectedId.value)

        with(actualGenre) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive)
            assertEquals(expectedCategories.asString(), categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
        }

        verify(exactly = 1) { genreGateway.findById(expectedId) }
    }

    @Test
    fun givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        // given
        val expectedId = GenreID.from("123")
        val expectedErrorMessage = "Genre with ID ${expectedId.value} was not found"

        every { genreGateway.findById(expectedId) } returns null

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(expectedId.value) }

        assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { genreGateway.findById(expectedId) }
    }

    private fun Iterable<CategoryID>.asString() = map { it.value }
}
