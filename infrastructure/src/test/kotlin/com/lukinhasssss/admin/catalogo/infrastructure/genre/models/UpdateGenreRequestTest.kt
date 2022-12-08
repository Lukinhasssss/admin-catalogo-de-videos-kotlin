package com.lukinhasssss.admin.catalogo.infrastructure.genre.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class UpdateGenreRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<UpdateGenreRequest>

    @Test
    fun testUnmarshall() {
        val expectedName = "Ação"
        val expectedCategory = "123"
        val expectedIsActive = false

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

// Unmarshall Desserializacao -> Conversao de string/json para objeto
