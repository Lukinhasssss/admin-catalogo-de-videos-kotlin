package com.lukinhasssss.admin.catalogo.infrastructure.castMember.models

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
class CastMemberListResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CastMemberListResponse>

    @Test
    fun testMarshall() {
        val expectId = "123"
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type().name
        val expectedCreatedAt = Instant.now()

        val response = CastMemberListResponse(
            id = expectId,
            name = expectedName,
            type = expectedType,
            createdAt = expectedCreatedAt.toString()
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.type", expectedType)
            .hasJsonPathValue("$.created_at", expectedCreatedAt)
    }
}

// Marshall (Serializacao) -> Conversao de um objeto para json
