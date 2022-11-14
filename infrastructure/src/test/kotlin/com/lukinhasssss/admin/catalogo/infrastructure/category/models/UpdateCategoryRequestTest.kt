package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class UpdateCategoryRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<UpdateCategoryRequest>

    @Test
    fun testUnmarshall() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val json = """
            {
                "name": "$expectedName",
                "description": "$expectedDescription",
                "is_active": "$expectedIsActive"
            }
        """.trimIndent()

        val actualJson = this.json.parse(json)

        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
    }
}

// Unmarshall Desserializacao -> Conversao de string/json para objeto
