package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateCategoryUseCaseTest {

    @InjectMockKs
    private lateinit var useCase: DefaultCreateCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() { clearAllMocks() }

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val aCommand = CreateCategoryCommand.with(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.create(any()) } answers { firstArg() }

        val actualOutput = useCase.execute(aCommand).get()

        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        verify(exactly = 1) {
            categoryGateway.create(
                withArg {
                    assertNotNull(it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedIsActive, it.isActive)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnDomainException() {
        val expectedName = "     "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val aCommand = CreateCategoryCommand.with(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val notification = useCase.execute(aCommand).left

        with(notification) {
            assertEquals(expectedErrorCount, getErrors().size)
            assertEquals(expectedErrorMessage, firstError().message)
        }

        verify(exactly = 0) { categoryGateway.create(any()) }
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val aCommand = CreateCategoryCommand.with(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.create(any()) } answers { firstArg() }

        val actualOutput = useCase.execute(aCommand).get()

        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        verify(exactly = 1) {
            categoryGateway.create(
                withArg {
                    assertNotNull(it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedIsActive, it.isActive)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                    assertNotNull(it.deletedAt)
                }
            )
        }
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "Gateway error"
        val expectedErrorCount = 1

        val aCommand = CreateCategoryCommand.with(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        every { categoryGateway.create(any()) } throws(IllegalStateException(expectedErrorMessage))

        val notification = useCase.execute(aCommand).left

        with(notification) {
            assertEquals(expectedErrorCount, getErrors().size)
            assertEquals(expectedErrorMessage, firstError().message)
        }

        verify(exactly = 1) {
            categoryGateway.create(
                withArg {
                    assertNotNull(it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedIsActive, it.isActive)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
        }
    }
}
