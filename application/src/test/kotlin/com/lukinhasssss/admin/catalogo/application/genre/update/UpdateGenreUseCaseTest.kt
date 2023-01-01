package com.lukinhasssss.admin.catalogo.application.genre.update

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class UpdateGenreUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultUpdateGenreUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @MockK
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val aGenre = Genre.newGenre("acao", true)

        val expectedId = aGenre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()

        val aCommand = UpdateGenreCommand.with(
            expectedId.value, expectedName, expectedIsActive, expectedCategories.asString()
        )

        every { genreGateway.findById(any()) } returns aGenre
        every { genreGateway.update(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        verify(exactly = 1) { genreGateway.findById(expectedId) }

        verify(exactly = 1) {
            genreGateway.update(
                withArg {
                    assertEquals(expectedId, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive())
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(aGenre.createdAt, it.createdAt)
                    assertTrue(aGenre.updatedAt.isBefore(it.updatedAt))
                    assertNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val aGenre = Genre.newGenre("acao", true)

        val expectedId = aGenre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )

        val aCommand = UpdateGenreCommand.with(
            expectedId.value, expectedName, expectedIsActive, expectedCategories.asString()
        )

        every { genreGateway.findById(any()) } returns aGenre
        every { categoryGateway.existsByIds(any()) } returns expectedCategories
        every { genreGateway.update(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        verify { genreGateway.findById(expectedId) }

        verify { categoryGateway.existsByIds(expectedCategories) }

        verify {
            genreGateway.update(
                withArg {
                    assertEquals(expectedId, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive())
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(aGenre.createdAt, it.createdAt)
                    assertTrue(aGenre.updatedAt.isBefore(it.updatedAt))
                    assertNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val aGenre = Genre.newGenre("acao", true)

        val expectedId = aGenre.id
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = emptyList<CategoryID>()

        val aCommand = UpdateGenreCommand.with(
            expectedId.value, expectedName, expectedIsActive, expectedCategories.asString()
        )

        every { genreGateway.findById(any()) } returns aGenre
        every { genreGateway.update(any()) } answers { firstArg() }

        assertTrue(aGenre.isActive())
        assertNull(aGenre.deletedAt)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        verify(exactly = 1) { genreGateway.findById(expectedId) }

        verify(exactly = 1) {
            genreGateway.update(
                withArg {
                    assertEquals(expectedId, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive())
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(aGenre.createdAt, it.createdAt)
                    assertTrue(aGenre.updatedAt.isBefore(it.updatedAt))
                    assertNotNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        val aGenre = Genre.newGenre("acao", true)

        val expectedId = aGenre.id
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val aCommand = UpdateGenreCommand.with(
            expectedId.value, expectedName, expectedIsActive, expectedCategories.asString()
        )

        every { genreGateway.findById(any()) } returns aGenre

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify(exactly = 1) { genreGateway.findById(expectedId) }
        verify(exactly = 0) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.update(any()) }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        // given
        val filmes = CategoryID.from("123")
        val series = CategoryID.from("456")
        val animes = CategoryID.from("789")

        val aGenre = Genre.newGenre("acao", true)

        val expectedId = aGenre.id
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = listOf(filmes, series, animes)
        val expectedErrorCount = 2
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be empty"

        val aCommand = UpdateGenreCommand.with(
            expectedId.value, expectedName, expectedIsActive, expectedCategories.asString()
        )

        every { genreGateway.findById(any()) } returns aGenre
        every { categoryGateway.existsByIds(any()) } returns listOf(filmes)

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessageOne, actualException.errors[0].message)
        assertEquals(expectedErrorMessageTwo, actualException.errors[1].message)

        verify(exactly = 1) { genreGateway.findById(expectedId) }
        verify(exactly = 1) { categoryGateway.existsByIds(expectedCategories) }
        verify(exactly = 0) { genreGateway.update(any()) }
    }
}
