package com.lukinhasssss.admin.catalogo.application.category.retrieve.get

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class GetCategoryByIdUseCaseIT {

    @Autowired
    private lateinit var useCase: GetCategoryByIdUseCase

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @SpykBean
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

        save(aCategory)

        val actualCategory = useCase.execute(expectedId.value)

        with(actualCategory) {
            assertEquals(expectedId.value, id.value)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedIsActive, isActive)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            // assertEquals(aCategory.updatedAt, updatedAt)
            // assertEquals(aCategory.updatedAt, updatedAt)
            assertNull(deletedAt)
        }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = CategoryID.from("123")
        val expectedErrorMessage = "Category with ID 123 was not found"

        val actualException = assertThrows<NotFoundException> { useCase.execute(expectedId.value) }

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

    private fun save(vararg aCategory: Category) {
        categoryRepository.saveAllAndFlush(
            aCategory.map(CategoryJpaEntity::from).toList()
        )
    }
}
