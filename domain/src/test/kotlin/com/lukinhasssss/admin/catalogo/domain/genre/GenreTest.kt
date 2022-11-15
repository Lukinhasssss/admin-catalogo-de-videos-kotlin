package com.lukinhasssss.admin.catalogo.domain.genre

import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
}
