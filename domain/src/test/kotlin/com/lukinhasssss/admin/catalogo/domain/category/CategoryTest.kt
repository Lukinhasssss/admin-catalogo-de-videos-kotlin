package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.UnitTest
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CategoryTest : UnitTest() {

    @Test
    fun givenAValidParams_whenCallNewCategory_thenShouldInstantiateACategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val actualCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        with(actualCategory) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "  "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val actualCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val actualException = assertThrows<DomainException> {
            actualCategory.validate(ThrowsValidationHandler())
        }

        with(actualException) {
            assertEquals(expectedErrorMessage, errors[0].message)
            assertEquals(expectedErrorCount, errors.count())
        }
    }

    @Test
    fun givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "Fi "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val expectedErrorCount = 1

        val actualCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val actualException = assertThrows<DomainException> {
            actualCategory.validate(ThrowsValidationHandler())
        }

        with(actualException) {
            assertEquals(expectedErrorMessage, errors[0].message)
            assertEquals(expectedErrorCount, errors.count())
        }
    }

    @Test
    fun givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "a".repeat(256)
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val expectedErrorCount = 1

        val actualCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val actualException = assertThrows<DomainException> {
            actualCategory.validate(ThrowsValidationHandler())
        }

        with(actualException) {
            assertEquals(expectedErrorMessage, errors[0].message)
            assertEquals(expectedErrorCount, errors.count())
        }
    }

    @Test
    fun givenAValidEmptyDescription_whenCallNewCategory_thenShouldInstantiateACategory() {
        val expectedName = "Filmes"
        val expectedDescription = "   "
        val expectedIsActive = true

        val actualCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        assertDoesNotThrow { actualCategory.validate(ThrowsValidationHandler()) }

        with(actualCategory) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidFalseIsActive_whenCallNewCategory_thenShouldInstantiateACategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val actualCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        assertDoesNotThrow { actualCategory.validate(ThrowsValidationHandler()) }

        with(actualCategory) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val aCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = true
        )

        assertDoesNotThrow { aCategory.validate(ThrowsValidationHandler()) }

        assertTrue(aCategory.isActive)
        assertNull(aCategory.deletedAt)

        val actualCategory = aCategory.deactivate()

        with(actualCategory) {
            assertEquals(aCategory.id, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(updatedAt.isAfter(aCategory.updatedAt))
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = false
        )

        assertDoesNotThrow { aCategory.validate(ThrowsValidationHandler()) }

        assertFalse(aCategory.isActive)
        assertNotNull(aCategory.deletedAt)

        val actualCategory = aCategory.activate()

        with(actualCategory) {
            assertEquals(aCategory.id, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(updatedAt.isAfter(aCategory.updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(
            aName = "Film",
            aDescription = "A categoria",
            isActive = expectedIsActive
        )

        assertDoesNotThrow { aCategory.validate(ThrowsValidationHandler()) }

        assertTrue(aCategory.isActive)
        assertNull(aCategory.deletedAt)

        val actualCategory = aCategory.update(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        with(actualCategory) {
            assertEquals(aCategory.id, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(updatedAt.isAfter(aCategory.updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAValidCategory_whenCallUpdateToInactivate_thenReturnCategoryUpdated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val aCategory = Category.newCategory(
            aName = "Film",
            aDescription = "A categoria",
            isActive = true
        )

        assertDoesNotThrow { aCategory.validate(ThrowsValidationHandler()) }

        assertTrue(aCategory.isActive)
        assertNull(aCategory.deletedAt)

        val actualCategory = aCategory.update(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        with(actualCategory) {
            assertEquals(aCategory.id, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(updatedAt.isAfter(aCategory.updatedAt))
            assertNotNull(deletedAt)
        }
    }

    @Test
    fun givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        val expectedName = "F"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(
            aName = "Film",
            aDescription = "A categoria",
            isActive = expectedIsActive
        )

        assertDoesNotThrow { aCategory.validate(ThrowsValidationHandler()) }

        assertTrue(aCategory.isActive)
        assertNull(aCategory.deletedAt)

        val actualCategory = aCategory.update(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        with(actualCategory) {
            assertEquals(aCategory.id, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertTrue(updatedAt.isAfter(aCategory.updatedAt))
            assertNull(deletedAt)
        }
    }
}
