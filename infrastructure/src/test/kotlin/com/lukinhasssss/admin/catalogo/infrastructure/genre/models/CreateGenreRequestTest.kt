package com.lukinhasssss.admin.catalogo.infrastructure.genre.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class CreateGenreRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<CreateGenreRequest>

    @Test
    fun testMarshall() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf("123", "456")

        val response = CreateGenreRequest(
            name = expectedName,
            active = expectedIsActive,
            categories = expectedCategories
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.categories_id", expectedCategories)
    }

    @Test
    fun testUnmarshall() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategory = "123"

        val json = """
            {
                "name": "$expectedName",
                "is_active": "$expectedIsActive",
                "categories_id": ["$expectedCategory"]
            }
        """.trimIndent()

        val actualJson = this.json.parse(json)

        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
            .hasFieldOrPropertyWithValue("categories", listOf(expectedCategory))
    }
}

// Marshall | Serializacao -> Conversao de um objeto para json/string...
// Unmarshall Desserializacao -> Conversao de string/json para objeto
