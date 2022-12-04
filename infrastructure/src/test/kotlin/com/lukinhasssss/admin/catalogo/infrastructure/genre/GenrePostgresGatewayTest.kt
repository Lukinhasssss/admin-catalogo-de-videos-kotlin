package com.lukinhasssss.admin.catalogo.infrastructure.genre

import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.infrastructure.category.CategoryPostgresGateway
import com.lukinhasssss.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@PostgresGatewayTest
class GenrePostgresGatewayTest {

    @Autowired
    private lateinit var categoryGateway: CategoryPostgresGateway

    @Autowired
    private lateinit var genreGateway: GenrePostgresGateway

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun testDependenciesInjected() {
        assertNotNull(categoryGateway)
        assertNotNull(genreGateway)
        assertNotNull(genreRepository)
    }

    @Test
    fun givenAValidGenre_whenCallsCreateGenre_shouldPersisteGenre() {
        // given
        val filmes = categoryGateway.create(Category.newCategory("Filmes", null, true))

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(filmes.id)

        val aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        // when
        val actualGenre = genreGateway.create(aGenre)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, getCategoryIDs())
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersisteGenre() {
        // given
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)

        val expectedId = aGenre.id

        assertEquals(0, genreRepository.count())

        // when
        val actualGenre = genreGateway.create(aGenre)

        // then
        assertEquals(1, genreRepository.count())

        with(actualGenre) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }

        val persistedGenre = genreRepository.findById(expectedId.value).get()

        with(persistedGenre) {
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, active)
            assertEquals(expectedCategories, getCategoryIDs())
            assertEquals(aGenre.createdAt, createdAt)
            assertEquals(aGenre.updatedAt, updatedAt)
            assertEquals(aGenre.deletedAt, deletedAt)
            assertNull(deletedAt)
        }
    }
}
