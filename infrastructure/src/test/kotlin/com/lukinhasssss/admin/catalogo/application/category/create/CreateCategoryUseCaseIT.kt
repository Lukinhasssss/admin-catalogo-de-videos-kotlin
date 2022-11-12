package com.lukinhasssss.admin.catalogo.application.category.create

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.Called
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class CreateCategoryUseCaseIT {

    @Autowired
    private lateinit var useCase: CreateCategoryUseCase

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @SpykBean
    private lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        assertEquals(0, categoryRepository.count())

        val aCommand = CreateCategoryCommand.with(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val actualOutput = useCase.execute(aCommand).get()

        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        assertEquals(1, categoryRepository.count())

        val actualCategory = categoryRepository.findById(actualOutput.id.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnDomainException() {
        val expectedName = "     "
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        assertEquals(0, categoryRepository.count())

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

        assertEquals(0, categoryRepository.count())

        verify { categoryGateway.create(any()) wasNot Called }
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        assertEquals(0, categoryRepository.count())

        val aCommand = CreateCategoryCommand.with(
            aName = expectedName,
            aDescription = expectedDescription,
            isActive = expectedIsActive
        )

        val actualOutput = useCase.execute(aCommand).get()

        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        assertEquals(1, categoryRepository.count())

        val actualCategory = categoryRepository.findById(actualOutput.id.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNotNull(deletedAt)
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
    }
}
