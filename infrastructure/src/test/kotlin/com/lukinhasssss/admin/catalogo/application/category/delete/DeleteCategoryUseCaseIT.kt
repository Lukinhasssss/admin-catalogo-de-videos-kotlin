package com.lukinhasssss.admin.catalogo.application.category.delete

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class DeleteCategoryUseCaseIT {

    @Autowired
    private lateinit var useCase: DeleteCategoryUseCase

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @SpykBean
    private lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id

        save(aCategory)

        assertEquals(1, categoryRepository.count())

        assertDoesNotThrow { useCase.execute(expectedId.value) }

        assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        val expectedId = CategoryID.from("123")

        assertEquals(0, categoryRepository.count())

        assertDoesNotThrow { useCase.execute(expectedId.value) }

        assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id

        every { categoryGateway.deleteById(any()) } throws IllegalStateException("Gateway error")

        assertThrows<IllegalStateException> { useCase.execute(expectedId.value) }

        verify(exactly = 1) {
            categoryGateway.deleteById(withArg { assertEquals(expectedId.value, it.value) })
        }
    }

    private fun save(vararg aCategory: Category) {
        categoryRepository.saveAllAndFlush(
            aCategory.map(CategoryJpaEntity::from).toList()
        )
    }
}
