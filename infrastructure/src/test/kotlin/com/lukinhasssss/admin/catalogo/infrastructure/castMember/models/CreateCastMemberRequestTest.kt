package com.lukinhasssss.admin.catalogo.infrastructure.castMember.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class CreateCastMemberRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<CreateCastMemberRequest>

    @Test
    fun testUnmarshall() {
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMembers.type()

        val json = """
            {
                "name": "$expectedName",
                "type": "$expectedType"
            }
        """.trimIndent()

        val actualJson = this.json.parse(json)

        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("type", expectedType)
    }
}

// Marshall | Serializacao -> Conversao de um objeto para json/string...
// Unmarshall Desserializacao -> Conversao de string/json para objeto
