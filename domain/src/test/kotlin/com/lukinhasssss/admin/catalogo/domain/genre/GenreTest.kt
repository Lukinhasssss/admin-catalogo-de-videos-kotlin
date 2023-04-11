package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.UnitTest
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GenreTest : UnitTest() {

    @Test
    fun givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = 0

        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        with(actualGenre) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories.size)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        val expectedName = "  "
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"

        val actualException = assertThrows<NotificationException> {
            Genre.newGenre(expectedName, expectedIsActive)
        }

        with(actualException.errors) {
            assertEquals(expectedErrorCount, size)
            assertEquals(expectedErrorMessage, first().message)
        }
    }

    @Test
    fun givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        val expectedName = "a".repeat(256)
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 1 and 255 characters"

        val actualException = assertThrows<NotificationException> {
            Genre.newGenre(expectedName, expectedIsActive)
        }

        with(actualException.errors) {
            assertEquals(expectedErrorCount, size)
            assertEquals(expectedErrorMessage, first().message)
        }
    }

    @Test
    fun givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = 0

        val aGenre = Genre.newGenre(expectedName, true)

        with(aGenre) {
            assertNotNull(this)
            assertTrue(isActive())
            assertNull(deletedAt)
        }

        val actualGenre = aGenre.deactivate()

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories.size)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = 0

        val aGenre = Genre.newGenre(expectedName, false)

        with(aGenre) {
            assertNotNull(this)
            assertFalse(isActive())
            assertNotNull(deletedAt)
        }

        val actualGenre = aGenre.activate()

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories.size)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(CategoryID.from("123"))

        val aGenre = Genre.newGenre("animacao", false)

        with(aGenre) {
            assertNotNull(this)
            assertFalse(isActive())
            assertNotNull(deletedAt)
        }

        val actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories)

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories.size, categories.size)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = mutableListOf(CategoryID.from("123"))

        val aGenre = Genre.newGenre("animacao", true)

        with(aGenre) {
            assertNotNull(this)
            assertTrue(isActive())
            assertNull(deletedAt)
        }

        val actualGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories)

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories.size, categories.size)
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
        val expectedName = "    "
        val expectedIsActive = true
        val expectedCategories = mutableListOf(CategoryID.from("123"))
        val expectedErrorCount = 1

        val actualGenre = Genre.newGenre("animacao", false)
        val expectedErrorMessage = "'name' should not be empty"

        val actualException = assertThrows<NotificationException> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories)
        }

        with(actualException.errors) {
            assertEquals(expectedErrorCount, size)
            assertEquals(expectedErrorMessage, first().message)
        }
    }

    @Test
    fun givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()

        val aGenre = Genre.newGenre("animacao", false)

        with(aGenre) {
            assertNotNull(this)
            assertFalse(isActive())
            assertNotNull(deletedAt)
        }

        val actualGenre = assertDoesNotThrow {
            aGenre.update(expectedName, expectedIsActive, null)
        }

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(seriesID, moviesID)

        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        assertTrue(actualGenre.categories.isEmpty())

        actualGenre.addCategory(seriesID)
        actualGenre.addCategory(moviesID)

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories)
            assertEquals(actualCreatedAt, createdAt)
            assertTrue(actualUpdatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = mutableListOf(moviesID)

        val aGenre = Genre.newGenre("animacao", false)
        val actualGenre = aGenre.update(expectedName, expectedIsActive, mutableListOf(seriesID, moviesID))

        assertEquals(2, actualGenre.categories.size)

        actualGenre.removeCategory(seriesID)

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories)
            assertEquals(aGenre.createdAt, createdAt)
            assertTrue(aGenre.updatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")

        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(seriesID, moviesID)

        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        assertTrue(actualGenre.categories.isEmpty())

        actualGenre.addCategories(expectedCategories)

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories)
            assertEquals(actualCreatedAt, createdAt)
            assertTrue(actualUpdatedAt.isBefore(updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = emptyList<CategoryID>()

        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        assertTrue(actualGenre.categories.isEmpty())

        actualGenre.addCategories(expectedCategories)

        with(actualGenre) {
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedIsActive, isActive())
            assertEquals(expectedCategories, categories)
            assertEquals(actualCreatedAt, createdAt)
            assertEquals(actualUpdatedAt, updatedAt)
            assertNull(deletedAt)
        }
    }
}
