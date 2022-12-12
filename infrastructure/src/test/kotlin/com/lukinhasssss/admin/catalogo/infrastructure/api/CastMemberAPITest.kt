package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberOutput
import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.delete.DeleteCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.GetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.ListCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.update.UpdateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.models.CreateCastMemberRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

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
        val expectedId = CastMemberID.from(UUID.randomUUID())
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aCommand = CreateCastMemberRequest(expectedName, expectedType)

        every { createCastMemberUseCase.execute(any()) } returns CreateCastMemberOutput.from(expectedId)

        // when
        val aResponse = mvc.post(urlTemplate = "/cast_members") {
            contentType = MediaType.APPLICATION_JSON
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
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(aCommand)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isUnprocessableEntity() }

            header {
                doesNotExist(name = "Location")
                string(name = "Content-Type", value = APPLICATION_JSON_VALUE)
            }

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
}
