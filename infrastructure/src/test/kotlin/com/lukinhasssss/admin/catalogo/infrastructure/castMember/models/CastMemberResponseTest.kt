package com.lukinhasssss.admin.catalogo.infrastructure.castMember.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
class CastMemberResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CastMemberResponse>

    @Test
    fun testMarshall() {
        val expectId = "123"
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMembers.type().name
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()

        val response = CastMemberResponse(
            id = expectId,
            name = expectedName,
            type = expectedType,
            createdAt = expectedCreatedAt.toString(),
            updatedAt = expectedUpdatedAt.toString()
        )

        val actualJson = json.write(response)

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.type", expectedType)
            .hasJsonPathValue("$.created_at", expectedCreatedAt)
            .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
    }
}

// Marshall (Serializacao) -> Conversao de um objeto para json
