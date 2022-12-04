package com.lukinhasssss.admin.catalogo.application.genre.retrieve.get

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class GetGenreByIdUseCaseIT {

    @Autowired
    private lateinit var useCase: GetGenreByIdUseCase

    @Autowired
    private lateinit var categoryGateway: CategoryGateway

    @Autowired
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // given
        val filmes =
            categoryGateway.create(Category.newCategory("Filmes", null, true))

        val series =
            categoryGateway.create(Category.newCategory("Series", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes.id, series.id)

        val aGenre = genreGateway.create(
            Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories)
        )

        val expectedId = aGenre.id

        // when
        val actualGenre = useCase.execute(expectedId.value)

        with(actualGenre) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive)
            assertTrue(
                expectedCategories.size == categories.size &&
                    expectedCategories.asString().containsAll(categories)
            )
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
        }
    }

    @Test
    fun givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        // given
        val expectedId = GenreID.from("123")
        val expectedErrorMessage = "Genre with ID ${expectedId.value} was not found"

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(expectedId.value) }

        assertEquals(expectedErrorMessage, actualException.message)
    }

    private fun Iterable<CategoryID>.asString() = map { it.value }
}
