package com.lukinhasssss.admin.catalogo.application.category.update

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class UpdateCategoryUseCaseTest {

    @InjectMockKs
    private lateinit var useCase: DefaultUpdateCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() { clearAllMocks() }

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCommand = UpdateCategoryCommand(
            id = expectedId.value,
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.findById(any()) } returns aCategory
        every { categoryGateway.update(any()) } answers { firstArg() }

        val actualOutput = useCase.execute(aCommand).get()

        assertNotNull(actualOutput)
        assertNotNull(actualOutput.id)

        verify(exactly = 1) { categoryGateway.findById(any()) }

        verify(exactly = 1) {
            categoryGateway.update(
                withArg {
                    with(it) {
                        assertEquals(expectedId, id)
                        assertEquals(expectedName, name)
                        assertEquals(expectedDescription, description)
                        assertEquals(expectedIsActive, isActive)
                        assertEquals(aCategory.createdAt, createdAt)
                        assertTrue(updatedAt.isAfter(aCategory.updatedAt))
                        assertNull(deletedAt)
                    }
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id
        val expectedName = "     "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val aCommand = UpdateCategoryCommand.with(
            anId = expectedId.value,
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.findById(any()) } returns aCategory

        val notification = useCase.execute(aCommand).left

        with(notification) {
            assertEquals(expectedErrorCount, getErrors().size)
            assertEquals(expectedErrorMessage, firstError().message)
        }

        verify(exactly = 0) { categoryGateway.update(any()) }
    }

    @Test
    fun givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val aCommand = UpdateCategoryCommand(
            id = expectedId.value,
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.findById(any()) } returns aCategory
        every { categoryGateway.update(any()) } answers { firstArg() }

        assertTrue(aCategory.isActive)
        assertNull(aCategory.deletedAt)

        val actualOutput = useCase.execute(aCommand).get()

        assertNotNull(actualOutput)
        assertNotNull(actualOutput.id)

        verify(exactly = 1) { categoryGateway.findById(any()) }

        verify(exactly = 1) {
            categoryGateway.update(
                withArg {
                    with(it) {
                        assertEquals(expectedId, id)
                        assertEquals(expectedName, name)
                        assertEquals(expectedDescription, description)
                        assertEquals(expectedIsActive, isActive)
                        assertEquals(aCategory.createdAt, createdAt)
                        assertTrue(updatedAt.isAfter(aCategory.updatedAt))
                        assertNotNull(deletedAt)
                    }
                }
            )
        }
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "Gateway error"
        val expectedErrorCount = 1

        val aCommand = UpdateCategoryCommand(
            id = expectedId.value,
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.findById(any()) } returns aCategory
        every { categoryGateway.update(any()) } throws(IllegalStateException(expectedErrorMessage))

        val notification = useCase.execute(aCommand).left

        with(notification) {
            assertEquals(expectedErrorCount, getErrors().size)
            assertEquals(expectedErrorMessage, firstError().message)
        }

        verify(exactly = 1) {
            categoryGateway.update(
                withArg {
                    with(it) {
                        assertEquals(expectedId, id)
                        assertEquals(expectedName, name)
                        assertEquals(expectedDescription, description)
                        assertEquals(expectedIsActive, isActive)
                        assertEquals(aCategory.createdAt, createdAt)
                        assertTrue(updatedAt.isAfter(aCategory.updatedAt))
                        assertNull(deletedAt)
                    }
                }
            )
        }
    }

    @Test
    fun givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFountException() {
        val expectedId = "not-found"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedErrorMessage = "Category with ID $expectedId was not found"
        val expectedErrorCount = 1

        val aCommand = UpdateCategoryCommand(
            id = expectedId,
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.findById(any()) } returns null

        val actualException = assertThrows<DomainException> { useCase.execute(aCommand) }

        with(actualException.errors) {
            assertEquals(expectedErrorCount, size)
            assertEquals(expectedErrorMessage, first().message)
        }

        verify(exactly = 1) { categoryGateway.findById(any()) }

        verify { categoryGateway.update(any()) wasNot Called }
    }
}
