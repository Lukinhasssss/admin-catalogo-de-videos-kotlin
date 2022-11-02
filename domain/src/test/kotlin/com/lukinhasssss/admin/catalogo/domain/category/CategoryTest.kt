package com.lukinhasssss.admin.catalogo.domain.category

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoryTest {

    @Test
    fun givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val actualCategory = Category.newCategory(
            aName = expectedName, aDescription = expectedDescription, isActive = expectedIsActive
        )

        assertNotNull(actualCategory)
        assertNotNull(actualCategory.id)
        assertEquals(expectedName, actualCategory.name)
        assertEquals(expectedDescription, actualCategory.description)
        assertEquals(expectedIsActive, actualCategory.isActive)
        assertNotNull(actualCategory.createdAt)
        assertNotNull(actualCategory.updatedAt)
        assertNull(actualCategory.deletedAt)
    }
}