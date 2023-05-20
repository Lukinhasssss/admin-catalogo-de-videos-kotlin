package com.lukinhasssss.admin.catalogo.application.genre.update

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class UpdateGenreUseCaseIT {

    @Autowired
    private lateinit var useCase: UpdateGenreUseCase

    @SpykBean
    private lateinit var categoryGateway: CategoryGateway

    @SpykBean
    private lateinit var genreGateway: GenreGateway

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val aGenre = genreGateway.create(Genre.newGenre("acao", true))

        val expectedId = aGenre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()

        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            expectedCategories.asString()
        )

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        val actualGenre = genreRepository.findById(expectedId.value).get()

        with(actualGenre) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertTrue(
                expectedCategories.size == getCategoryIDs().size &&
                    expectedCategories.containsAll(getCategoryIDs())
            )
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val filmes =
            categoryGateway.create(Category.newCategory("Filmes", null, true))

        val series =
            categoryGateway.create(Category.newCategory("Series", null, true))

        val aGenre = genreGateway.create(Genre.newGenre("acao", true))

        val expectedId = aGenre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes.id, series.id)

        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            expectedCategories.asString()
        )

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        val actualGenre = genreRepository.findById(expectedId.value).get()

        with(actualGenre) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertTrue(
                expectedCategories.size == getCategoryIDs().size &&
                    expectedCategories.containsAll(getCategoryIDs())
            )
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        val aGenre = genreGateway.create(Genre.newGenre("acao", true))

        val expectedId = aGenre.id
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = emptyList<CategoryID>()

        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            expectedCategories.asString()
        )

        assertTrue(aGenre.isActive())
        assertNull(aGenre.deletedAt)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        val actualGenre = genreRepository.findById(expectedId.value).get()

        with(actualGenre) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertTrue(
                expectedCategories.size == getCategoryIDs().size &&
                    expectedCategories.containsAll(getCategoryIDs())
            )
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        val aGenre = genreGateway.create(Genre.newGenre("acao", true))

        val expectedId = aGenre.id
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            expectedCategories.asString()
        )

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
        val filmes =
            categoryGateway.create(Category.newCategory("Filmes", null, true))

        val series = CategoryID.from("456")
        val animes = CategoryID.from("789")

        val aGenre = genreGateway.create(Genre.newGenre("acao", true))

        val expectedId = aGenre.id
        val expectedName = "   "
        val expectedIsActive = true
        val expectedCategories = listOf(filmes.id, series, animes)
        val expectedErrorCount = 2
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be empty"

        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            expectedCategories.asString()
        )

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

    private fun Iterable<CategoryID>.asString() = map { it.value }
}
