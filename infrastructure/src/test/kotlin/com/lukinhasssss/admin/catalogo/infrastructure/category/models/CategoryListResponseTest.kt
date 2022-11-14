package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
class CategoryListResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CategoryListResponse>

    @Test
    fun testMarshall() {
        val expectId = "123"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val response = CategoryListResponse(
            id = expectId,
            name = expectedName,
            description = expectedDescription,
            active = expectedIsActive,
            createdAt = expectedCreatedAt,
            deletedAt = expectedDeletedAt
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
    }
}

// Marshall | Serializacao -> Conversao de um objeto para json/string...
