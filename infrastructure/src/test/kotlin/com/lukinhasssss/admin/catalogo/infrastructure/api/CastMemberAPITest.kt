package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberOutput
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.delete.DeleteCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.CastMemberOutput
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.GetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.CastMemberListOutput
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.ListCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.update.UpdateCastMemberOutput
import com.lukinhasssss.admin.catalogo.application.castMember.update.UpdateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.UpdateCastMemberRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@ControllerTest(controllers = [CastMemberAPI::class])
class CastMemberAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var createCastMemberUseCase: CreateCastMemberUseCase

    @MockkBean
    private lateinit var deleteCastMemberUseCase: DeleteCastMemberUseCase

    @MockkBean
    private lateinit var getCastMemberByIdUseCase: GetCastMemberByIdUseCase

    @MockkBean
    private lateinit var listCastMemberUseCase: ListCastMemberUseCase

    @MockkBean
    private lateinit var updateCastMemberUseCase: UpdateCastMemberUseCase

    @Test
    fun givenAValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() {
        // given
        val expectedId = CastMemberID.from(IdUtils.uuid())
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aCommand = CreateCastMemberRequest(expectedName, expectedType)

        every { createCastMemberUseCase.execute(any()) } returns CreateCastMemberOutput.from(expectedId)

        // when
        val aResponse = mvc.post(urlTemplate = "/cast_members") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isCreated() }

            header {
                string(name = "Location", value = "/cast_members/${expectedId.value}")
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId.value))
        }

        verify {
            createCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() {
        // given
        val expectedName = "   "
        val expectedType = Fixture.CastMember.type()
        val expectedErrorMessage = "'name' should not be empty"

        val aCommand = CreateCastMemberRequest(expectedName, expectedType)

        every {
            createCastMemberUseCase.execute(any())
        } throws NotificationException("", Notification.create(Error(expectedErrorMessage)))

        // when
        val aResponse = mvc.post(urlTemplate = "/cast_members") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isUnprocessableEntity() }

            header {
                doesNotExist(name = "Location")
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.errors.size()", equalTo(1))
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
        }

        verify {
            createCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                }
            )
        }
    }

    @Test
    fun givenAValidId_whenCallsGetById_shouldReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aMember = CastMember.newMember(expectedName, expectedType)

        val expectedId = aMember.id.value

        every { getCastMemberByIdUseCase.execute(any()) } returns CastMemberOutput.from(aMember)

        // when
        val aResponse = mvc.get(urlTemplate = "/cast_members/$expectedId").andDo { print() }

        // then
        aResponse.andExpect {
            status { isOk() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId))
            jsonPath("$.name", equalTo(expectedName))
            jsonPath("$.type", equalTo(expectedType.name))
            jsonPath("$.created_at", equalTo(aMember.createdAt.toString()))
            jsonPath("$.updated_at", equalTo(aMember.updatedAt.toString()))
        }

        verify { getCastMemberByIdUseCase.execute(expectedId) }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetByIdAndCastMemberDoesntExists_shouldReturnNotFound() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedErrorMessage = "CastMember with ID 123 was not found"

        every {
            getCastMemberByIdUseCase.execute(any())
        } throws NotFoundException.with(id = expectedId, anAggregate = CastMember::class)

        // when
        val aResponse = mvc.get(urlTemplate = "/cast_members/${expectedId.value}").andDo { print() }

        // then
        aResponse.andExpect {
            status { isNotFound() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.message", equalTo(expectedErrorMessage))
        }

        verify { getCastMemberByIdUseCase.execute(expectedId.value) }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        val expectedId = aMember.id
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aCommand = UpdateCastMemberRequest(expectedName, expectedType)

        every { updateCastMemberUseCase.execute(any()) } returns UpdateCastMemberOutput.from(expectedId)

        // when
        val aResponse = mvc.put(urlTemplate = "/cast_members/${expectedId.value}") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isOk() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId.value))
        }

        verify {
            updateCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedId.value, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateCastMember_shouldReturnNotification() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        val expectedId = aMember.id
        val expectedName = "   "
        val expectedType = Fixture.CastMember.type()
        val expectedErrorMessage = "'name' should not be empty"

        val aCommand = UpdateCastMemberRequest(expectedName, expectedType)

        every {
            updateCastMemberUseCase.execute(any())
        } throws NotificationException("", Notification.create(Error(expectedErrorMessage)))

        // when
        val aResponse = mvc.put(urlTemplate = "/cast_members/${expectedId.value}") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isUnprocessableEntity() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.errors.size()", equalTo(1))
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
        }

        verify {
            updateCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedId.value, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidId_whenCallsUpdateCastMemberAndCastMemberDoesntExists_shouldReturnNotFound() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()
        val expectedErrorMessage = "CastMember with ID 123 was not found"

        val aCommand = UpdateCastMemberRequest(expectedName, expectedType)

        every {
            updateCastMemberUseCase.execute(any())
        } throws NotFoundException.with(id = expectedId, anAggregate = CastMember::class)

        // when
        val aResponse = mvc.put(urlTemplate = "/cast_members/${expectedId.value}") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isNotFound() }

            header {
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

            jsonPath("$.message", equalTo(expectedErrorMessage))
        }

        verify {
            updateCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedId.value, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                }
            )
        }
    }

    @Test
    fun givenAValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        val expectedId = "123"

        every { deleteCastMemberUseCase.execute(any()) } returns Unit

        // when
        val aResponse = mvc.delete(urlTemplate = "/cast_members/$expectedId").andDo { print() }

        // then
        aResponse.andExpect { status { isNoContent() } }

        verify { deleteCastMemberUseCase.execute(expectedId) }
    }

    @Test
    fun givenValidParams_whenCallsListCastMembers_shouldReturnIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type())

        val expectedPage = 0
        val expectedPerPage = 20
        val expectedTerms = "Alg"
        val expectedSort = "type"
        val expectedDirection = "desc"
        val expectedItemsCount = 1
        val expectedTotal = 1
        val expectedItems = listOf(CastMemberListOutput.from(aMember))

        every {
            listCastMemberUseCase.execute(any())
        } returns Pagination(expectedPage, expectedPerPage, expectedTotal.toLong(), expectedItems)

        // when
        val aResponse = mvc.get(urlTemplate = "/cast_members") {
            param("page", expectedPage.toString())
            param("perPage", expectedPerPage.toString())
            param("sort", expectedSort)
            param("dir", expectedDirection)
            param("search", expectedTerms)
        }

        // then
        aResponse.andExpect {
            status { isOk() }
            jsonPath("$.current_page", equalTo(expectedPage))
            jsonPath("$.per_page", equalTo(expectedPerPage))
            jsonPath("$.total", equalTo(expectedTotal))
            jsonPath("$.items.size()", equalTo(expectedItemsCount))
            jsonPath("$.items[0].id", equalTo(aMember.id.value))
            jsonPath("$.items[0].name", equalTo(aMember.name))
            jsonPath("$.items[0].type", equalTo(aMember.type.toString()))
            jsonPath("$.items[0].created_at", equalTo(aMember.createdAt.toString()))
        }

        verify {
            listCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedPage, it.page)
                    assertEquals(expectedPerPage, it.perPage)
                    assertEquals(expectedDirection, it.direction)
                    assertEquals(expectedSort, it.sort)
                    assertEquals(expectedTerms, it.terms)
                }
            )
        }
    }

    @Test
    fun givenEmptyParams_whenCallsListCastMembers_shouldUseDefaultsAndReturnIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type())

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedItemsCount = 1
        val expectedTotal = 1
        val expectedItems = listOf(CastMemberListOutput.from(aMember))

        every {
            listCastMemberUseCase.execute(any())
        } returns Pagination(expectedPage, expectedPerPage, expectedTotal.toLong(), expectedItems)

        // when
        val aResponse = mvc.get(urlTemplate = "/cast_members")

        // then
        aResponse.andExpect {
            status { isOk() }
            jsonPath("$.current_page", equalTo(expectedPage))
            jsonPath("$.per_page", equalTo(expectedPerPage))
            jsonPath("$.total", equalTo(expectedTotal))
            jsonPath("$.items.size()", equalTo(expectedItemsCount))
            jsonPath("$.items[0].id", equalTo(aMember.id.value))
            jsonPath("$.items[0].name", equalTo(aMember.name))
            jsonPath("$.items[0].type", equalTo(aMember.type.toString()))
            jsonPath("$.items[0].created_at", equalTo(aMember.createdAt.toString()))
        }

        verify {
            listCastMemberUseCase.execute(
                withArg {
                    assertEquals(expectedPage, it.page)
                    assertEquals(expectedPerPage, it.perPage)
                    assertEquals(expectedDirection, it.direction)
                    assertEquals(expectedSort, it.sort)
                    assertEquals(expectedTerms, it.terms)
                }
            )
        }
    }
}
