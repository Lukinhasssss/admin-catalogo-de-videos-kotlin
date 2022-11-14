package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class CreateCategoryRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<CreateCategoryRequest>

    @Test
    fun testMarshall() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val response = CreateCategoryRequest(
            name = expectedName,
            description = expectedDescription,
            active = expectedIsActive
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedIsActive)
    }

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

// Marshall | Serializacao -> Conversao de um objeto para json/string...
// Unmarshall Desserializacao -> Conversao de string/json para objeto
