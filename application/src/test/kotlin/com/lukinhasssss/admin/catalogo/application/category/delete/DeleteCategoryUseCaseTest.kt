package com.lukinhasssss.admin.catalogo.application.category.delete

import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class DeleteCategoryUseCaseTest {

    @InjectMockKs
    private lateinit var useCase: DefaultDeleteCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() { clearAllMocks() }

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val aCategory = Category.newCategory(aName = "Filme", aDescription = null, isActive = true)

        val expectedId = aCategory.id

        every { categoryGateway.deleteById(any()) } returns Unit

        assertDoesNotThrow { useCase.execute(expectedId.value) }

        verify(exactly = 1) {
            categoryGateway.deleteById(withArg { assertEquals(expectedId.value, it.value) })
        }
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        val expectedId = CategoryID.from("123")

        every { categoryGateway.deleteById(any()) } returns Unit

        assertDoesNotThrow { useCase.execute(expectedId.value) }

        verify(exactly = 1) {
            categoryGateway.deleteById(withArg { assertEquals(expectedId.value, it.value) })
        }
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
}
