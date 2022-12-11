package com.lukinhasssss.admin.catalogo.application.genre.create

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateGenreUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultCreateGenreUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @MockK
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf<CategoryID>()

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        every { genreGateway.create(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        verify(exactly = 1) {
            genreGateway.create(
                withArg {
                    assertNotNull(it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive())
                    assertEquals(expectedCategories, it.categories)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        every { categoryGateway.existsByIds(any()) } returns expectedCategories

        every { genreGateway.create(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        verify(exactly = 1) {
            categoryGateway.existsByIds(
                withArg {
                    assertEquals(expectedCategories, it)
                }
            )
        }

        verify(exactly = 1) {
            genreGateway.create(
                withArg {
                    assertNotNull(it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive())
                    assertEquals(expectedCategories, it.categories)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        with(actualException) {
            assertNotNull(this)
            assertEquals(expectedErrorCount, errors.size)
            assertEquals(expectedErrorMessage, errors.first().message)
        }

        verify(exactly = 0) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.create(any()) }
    }

    @Test
    fun givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        val series = CategoryID.from("123")
        val filmes = CategoryID.from("456")
        val animes = CategoryID.from("789")

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes, series, animes)
        val expectedErrorMessage = "Some categories could not be found: 456, 789"
        val expectedErrorCount = 1

        every { categoryGateway.existsByIds(any()) } returns listOf(series)

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        with(actualException) {
            assertNotNull(this)
            assertEquals(expectedErrorCount, errors.size)
            assertEquals(expectedErrorMessage, errors.first().message)
        }

        verify(exactly = 1) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.create(any()) }
    }

    @Test
    fun givenAnInvalidEmptyName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        val series = CategoryID.from("123")
        val filmes = CategoryID.from("456")
        val animes = CategoryID.from("789")

        val expectedName = "  "
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes, series, animes)
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be empty"
        val expectedErrorCount = 2

        every { categoryGateway.existsByIds(any()) } returns listOf(series)

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        with(actualException) {
            assertNotNull(this)
            assertEquals(expectedErrorCount, errors.size)
            assertEquals(expectedErrorMessageOne, errors[0].message)
            assertEquals(expectedErrorMessageTwo, errors[1].message)
        }

        verify(exactly = 1) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.create(any()) }
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = mutableListOf<CategoryID>()

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        every { genreGateway.create(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        verify(exactly = 1) {
            genreGateway.create(
                withArg {
                    assertNotNull(it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedIsActive, it.isActive())
                    assertEquals(expectedCategories, it.categories)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                    assertNotNull(it.deletedAt)
                }
            )
        }
    }

    private fun Iterable<CategoryID>.asString() = map { it.value }
}
