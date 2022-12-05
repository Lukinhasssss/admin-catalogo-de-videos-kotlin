package com.lukinhasssss.admin.catalogo.application.genre.create

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
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
class CreateGenreUseCaseIT {

    @Autowired
    private lateinit var useCase: CreateGenreUseCase

    @SpykBean
    private lateinit var categoryGateway: CategoryGateway

    @SpykBean
    private lateinit var genreGateway: GenreGateway

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val filmes =
            categoryGateway.create(Category.newCategory("Filmes", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes.id)

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        val actualGenre = genreRepository.findById(actualOutput.id).get()

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertTrue(
                expectedCategories.size == getCategoryIDs().size &&
                    expectedCategories.containsAll(getCategoryIDs())
            )
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
        // given
        val filmes = Category.newCategory("Filmes", "Os melhores filmes", true)
        val series = Category.newCategory("Series", "A categoria mais assistida", true)
        val animes = Category.newCategory("Animes", "Os animes mais assistidos", true)

        assertEquals(0, categoryRepository.count())

        categoryRepository.saveAllAndFlush(
            listOf(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(animes)
            )
        )

        assertEquals(3, categoryRepository.count())

        val expectedIds = listOf(filmes.id, series.id, animes.id)

        val ids = listOf(filmes.id, series.id, animes.id, CategoryID.from("123"))

        // when
        val actualResult = categoryGateway.existsByIds(ids)

        // then
        assertEquals(sorted(expectedIds), sorted(actualResult))
    }

    @Test
    fun givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        val actualGenre = genreRepository.findById(actualOutput.id).get()

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertTrue(
                expectedCategories.size == getCategoryIDs().size &&
                    expectedCategories.containsAll(getCategoryIDs())
            )
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = mutableListOf<CategoryID>()

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, expectedCategories.asString())

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        val actualGenre = genreRepository.findById(actualOutput.id).get()

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertTrue(
                expectedCategories.size == getCategoryIDs().size &&
                    expectedCategories.containsAll(getCategoryIDs())
            )
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNotNull(deletedAt)
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
    fun givenAnInvalidEmptyName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        val filmes =
            categoryGateway.create(Category.newCategory("Filmes", null, true))

        val series = CategoryID.from("456")
        val animes = CategoryID.from("789")

        val expectedName = "  "
        val expectedIsActive = true
        val expectedCategories = mutableListOf(filmes.id, series, animes)
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be empty"
        val expectedErrorCount = 2

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

    private fun Iterable<CategoryID>.asString() = map { it.value }

    private fun sorted(expectedCategories: List<CategoryID>) = expectedCategories.sortedBy { it.value }
}
