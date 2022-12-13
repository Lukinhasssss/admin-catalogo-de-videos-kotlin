package com.lukinhasssss.admin.catalogo.infrastructure.castMember.models

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class UpdateCastMemberRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<UpdateCastMemberRequest>

    @Test
    fun testUnmarshall() {
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

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

// Unmarshall (Desserializacao) -> Conversao de string/json para objeto
