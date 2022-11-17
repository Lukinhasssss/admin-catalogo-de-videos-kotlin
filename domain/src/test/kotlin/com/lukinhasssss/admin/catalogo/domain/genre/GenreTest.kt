package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GenreTest {

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
}
