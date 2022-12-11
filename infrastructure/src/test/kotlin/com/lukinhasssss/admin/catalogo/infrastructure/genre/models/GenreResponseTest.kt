package com.lukinhasssss.admin.catalogo.infrastructure.genre.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
class GenreResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<GenreResponse>

    @Test
    fun testMarshall() {
        val expectId = "123"
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf("123")
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val response = GenreResponse(
            id = expectId,
            name = expectedName,
            active = expectedIsActive,
            categories = expectedCategories,
            createdAt = expectedCreatedAt,
            updatedAt = expectedUpdatedAt,
            deletedAt = expectedDeletedAt
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.categories_id", expectedCategories)
            .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
            .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
    }

    @Test
    fun testUnmarshall() {
        val expectId = "123"
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategory = "123"
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val json = """
            {
                "id": "$expectId",
                "name": "$expectedName",
                "categories_id": ["$expectedCategory"],
                "is_active": "$expectedIsActive",
                "created_at": "$expectedCreatedAt",
                "updated_at": "$expectedUpdatedAt",
                "deleted_at": "$expectedDeletedAt"
            }
        """.trimIndent()

        val actualJson = this.json.parse(json)

        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
            .hasFieldOrPropertyWithValue("categories", listOf(expectedCategory))
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
    }
}

// Marshall   | Serializacao -> Conversao de um objeto para json/string
// Unmarshall | Desserializacao -> Conversao de string/json para objeto
