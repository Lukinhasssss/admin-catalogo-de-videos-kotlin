package com.lukinhasssss.admin.catalogo.application.category.retrieve.get

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class)
class GetCategoryByIdUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultGetCategoryByIdUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCategory = Category.newCategory(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val expectedId = aCategory.id

        every { categoryGateway.findById(any()) } returns aCategory

        val actualCategory = useCase.execute(expectedId.value)

        with(actualCategory) {
            assertEquals(CategoryOutput.from(aCategory), this)
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertEquals(aCategory.createdAt, createdAt)
            assertEquals(aCategory.updatedAt, updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = CategoryID.from("123")
        val expectedErrorMessage = "Category with ID 123 was not found"

        every { categoryGateway.findById(any()) } returns null

        val actualException = assertThrows<DomainException> { useCase.execute(expectedId.value) }

        assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val expectedId = CategoryID.from("not-found")
        val expectedErrorMessage = "Gateway error"

        every { categoryGateway.findById(any()) } throws IllegalStateException(expectedErrorMessage)

        val actualException = assertThrows<IllegalStateException> { useCase.execute(expectedId.value) }

        assertEquals(expectedErrorMessage, actualException.message)
    }
}
