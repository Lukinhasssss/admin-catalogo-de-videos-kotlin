package com.lukinhasssss.admin.catalogo.infrastructure.genre.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
class GenreListResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<GenreListResponse>

    @Test
    fun testMarshall() {
        val expectId = "123"
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val response = GenreListResponse(
            id = expectId,
            name = expectedName,
            active = expectedIsActive,
            createdAt = expectedCreatedAt,
            deletedAt = expectedDeletedAt
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
    }
}

// Marshall | Serializacao -> Conversao de um objeto para json/string
