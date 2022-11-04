package com.lukinhasssss.admin.catalogo.domain.category

import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CategoryTest {

    @Test
    fun givenAValidParams_whenCallNewCategory_thenShouldInstantiateACategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val actualCategory = Category.newCategory(
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
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
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
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
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
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
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
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
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
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
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
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
}
