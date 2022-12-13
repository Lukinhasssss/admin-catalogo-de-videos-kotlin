package com.lukinhasssss.admin.catalogo.e2e.castMember

import com.lukinhasssss.admin.catalogo.E2ETest
import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.e2e.MockDsl
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import io.restassured.module.kotlin.extensions.Then
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
@Testcontainers
class CastMemberE2ETest : MockDsl {

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    companion object {
        @Container
        val POSTGRESQL_CONTAINER: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:alpine")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("adm_videos")

        /* Este metodo serve para customizar as properties do spring em runtime com base no que o container vai gerar */
        @JvmStatic
        @DynamicPropertySource
        fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("postgres.port") { POSTGRESQL_CONTAINER.getMappedPort(5432) }
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val actualMemberId = givenACastMember(expectedName, expectedType)

        val actualMember = castMemberRepository.findById(actualMemberId.value).get()

        with(actualMember) {
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertEquals(createdAt, updatedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInvalidValues() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedName = "  "
        val expectedType = Fixture.CastMember.type()
        val expectedStatusCode = UNPROCESSABLE_ENTITY.value()
        val expectedErrorMessage = "'name' should not be empty"

        givenACastMemberResponse(expectedName, expectedType) Then {
            statusCode(expectedStatusCode)

            header("Location", nullValue())
            header("Content-Type", APPLICATION_JSON_VALUE)
            body("errors.size()", equalTo(1))
            body("errors[0].message", equalTo(expectedErrorMessage))
        }
    }
}
