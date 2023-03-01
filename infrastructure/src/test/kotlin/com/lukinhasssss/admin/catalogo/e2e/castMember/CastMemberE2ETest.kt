package com.lukinhasssss.admin.catalogo.e2e.castMember

import com.lukinhasssss.admin.catalogo.E2ETest
import com.lukinhasssss.admin.catalogo.KeycloakTestContainers
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType.ACTOR
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType.DIRECTOR
import com.lukinhasssss.admin.catalogo.e2e.MockDsl
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import io.restassured.module.kotlin.extensions.Then
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
@Testcontainers
class CastMemberE2ETest : MockDsl, KeycloakTestContainers {

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
        val expectedType = Fixture.CastMembers.type()

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
        val expectedType = Fixture.CastMembers.type()
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

    @Test
    fun asACatalogAdminIShouldBeAbleToNavigateThroughAllMembers() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedStatusCode = OK.value()

        givenACastMember("Vin Diesel", ACTOR)
        givenACastMember("Quentin Tarantino", DIRECTOR)
        givenACastMember("Jason Momoa", ACTOR)

        listCastMembers(page = 0, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Jason Momoa"))
        }

        listCastMembers(page = 1, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(1))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Quentin Tarantino"))
        }

        listCastMembers(page = 2, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(2))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Vin Diesel"))
        }

        listCastMembers(page = 3, perPage = 1) Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(3))
            body("per_page", equalTo(1))
            body("total", equalTo(3))
            // body("$", Matchers.not(Matchers.hasKey("items")))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSearchThroughAllMembers() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedStatusCode = OK.value()

        givenACastMember("Vin Diesel", ACTOR)
        givenACastMember("Quentin Tarantino", DIRECTOR)
        givenACastMember("Jason Momoa", ACTOR)

        listCastMembers(page = 0, perPage = 1, search = "vin") Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(1))
            body("total", equalTo(1))
            body("items.size()", equalTo(1))
            body("items.name", hasItem("Vin Diesel"))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeToSortAllMembersByNameDesc() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedStatusCode = OK.value()

        givenACastMember("Vin Diesel", ACTOR)
        givenACastMember("Quentin Tarantino", DIRECTOR)
        givenACastMember("Jason Momoa", ACTOR)

        listCastMembers(page = 0, perPage = 3, search = "", sort = "name", direction = "desc") Then {
            statusCode(expectedStatusCode)
            body("current_page", equalTo(0))
            body("per_page", equalTo(3))
            body("total", equalTo(3))
            body("items.size()", equalTo(3))
            body("items.get(0).name", equalTo("Vin Diesel"))
            body("items.get(1).name", equalTo("Quentin Tarantino"))
            body("items.get(2).name", equalTo("Jason Momoa"))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMembers.type()

        givenACastMember(Fixture.name(), Fixture.CastMembers.type())
        givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        val actualId = givenACastMember(expectedName, expectedType)

        val actualMember = retrieveACastMember(actualId)

        with(actualMember) {
            assertEquals(expectedName, name)
            assertEquals(expectedType.name, type)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertEquals(createdAt, updatedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedStatusCode = NOT_FOUND.value()
        val expectedMessage = "CastMember with ID 123 was not found"

        givenACastMember(Fixture.name(), Fixture.CastMembers.type())
        givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        retrieveACastMemberResponse(CastMemberID.from("123")) Then {
            statusCode(expectedStatusCode)
            body("message", equalTo(expectedMessage))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedName = "Vin Diesel"
        val expectedType = ACTOR
        val expectedStatusCode = OK.value()

        givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        val actualId = givenACastMember("A Pedra", DIRECTOR)

        updateACastMember(actualId, expectedName, expectedType) Then {
            statusCode(expectedStatusCode)
        }

        val actualMember = retrieveACastMember(actualId)

        with(actualMember) {
            assertEquals(expectedName, name)
            assertEquals(expectedType.name, type)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSeeATreatedErrorByUpdatingANewCastMemberWithInvalidValues() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedName = "  "
        val expectedType = Fixture.CastMembers.type()
        val expectedStatusCode = UNPROCESSABLE_ENTITY.value()
        val expectedErrorMessage = "'name' should not be empty"

        givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        val actualId = givenACastMember("A Pedra", DIRECTOR)

        updateACastMember(actualId, expectedName, expectedType) Then {
            statusCode(expectedStatusCode)
            body("errors.size()", equalTo(1))
            body("errors[0].message", equalTo(expectedErrorMessage))
        }
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedStatusCode = NO_CONTENT.value()

        givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        val actualId = givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        assertEquals(2, castMemberRepository.count())

        deleteACastMember(actualId) Then { statusCode(expectedStatusCode) }

        assertEquals(1, castMemberRepository.count())
        assertFalse(castMemberRepository.existsById(actualId.value))
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToDeleteACastMemberWithInvalidIdentifier() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning)
        assertEquals(0, castMemberRepository.count())

        val expectedStatusCode = NO_CONTENT.value()

        givenACastMember(Fixture.name(), Fixture.CastMembers.type())
        givenACastMember(Fixture.name(), Fixture.CastMembers.type())

        assertEquals(2, castMemberRepository.count())

        deleteACastMember(CastMemberID.from("123")) Then { statusCode(expectedStatusCode) }

        assertEquals(2, castMemberRepository.count())
    }
}
