package com.lukinhasssss.admin.catalogo.application.category.update

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.Called
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class UpdateCategoryUseCaseIT {

    @Autowired
    private lateinit var useCase: UpdateCategoryUseCase

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @SpykBean
    lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        save(aCategory)

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

        assertEquals(1, categoryRepository.count())

        val actualOutput = useCase.execute(aCommand).get()

        with(actualOutput) {
            assertNotNull(this)
            assertNotNull(id)
        }

        assertEquals(1, categoryRepository.count())

        val actualCategory = categoryRepository.findById(expectedId.value).get()

        with(actualCategory) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertTrue(updatedAt.isAfter(aCategory.updatedAt))
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        save(aCategory)

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

        val notification = useCase.execute(aCommand).left

        with(notification) {
            assertEquals(expectedErrorCount, getErrors().size)
            assertEquals(expectedErrorMessage, firstError().message)
        }

        verify { categoryGateway.update(any()) wasNot Called }
    }

    @Test
    fun givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        save(aCategory)

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

        assertEquals(1, categoryRepository.count())

        assertTrue(aCategory.isActive)
        assertNull(aCategory.deletedAt)

        val actualOutput = useCase.execute(aCommand).get()

        assertNotNull(actualOutput)
        assertNotNull(actualOutput.id)

        assertEquals(1, categoryRepository.count())

        val actualCategory = categoryRepository.findById(expectedId.value).get()

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
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        save(aCategory)

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

        every { categoryGateway.update(any()) } throws IllegalStateException(expectedErrorMessage)

        val notification = useCase.execute(aCommand).left

        with(notification) {
            assertEquals(expectedErrorCount, getErrors().size)
            assertEquals(expectedErrorMessage, firstError().message)
        }

        val actualCategory = categoryRepository.findById(expectedId.value).get()

        with(actualCategory) {
            assertEquals(aCategory.name, name)
            assertEquals(aCategory.description, description)
            assertEquals(aCategory.isActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertNull(deletedAt)
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

        val actualException = assertThrows<DomainException> { useCase.execute(aCommand) }

        with(actualException.errors) {
            assertEquals(expectedErrorCount, size)
            assertEquals(expectedErrorMessage, first().message)
        }
    }

    private fun save(vararg aCategory: Category) {
        categoryRepository.saveAllAndFlush(
            aCategory.map(CategoryJpaEntity::from).toList()
        )
    }
}
