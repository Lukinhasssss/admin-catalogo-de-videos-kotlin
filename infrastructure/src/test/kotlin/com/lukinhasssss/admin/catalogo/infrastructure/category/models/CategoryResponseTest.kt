package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
class CategoryResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CategoryResponse>

    @Test
    fun testMarshall() {
        val expectId = "123"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val response = CategoryResponse(
            id = expectId,
            name = expectedName,
            description = expectedDescription,
            active = expectedIsActive,
            createdAt = expectedCreatedAt,
            updatedAt = expectedUpdatedAt,
            deletedAt = expectedDeletedAt
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
            .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
    }

    @Test
    fun testUnmarshall() {
        val expectId = "123"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val json = """
            {
                "id": "$expectId",
                "name": "$expectedName",
                "description": "$expectedDescription",
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
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
    }
}

// Marshall | Serializacao -> Conversao de um objeto para json/string...
// Unmarshall Desserializacao -> Conversao de string/json para objeto
