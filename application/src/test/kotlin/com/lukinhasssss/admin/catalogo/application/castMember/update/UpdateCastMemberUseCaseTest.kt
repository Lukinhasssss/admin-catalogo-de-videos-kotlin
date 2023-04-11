package com.lukinhasssss.admin.catalogo.application.castMember.update

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UpdateCastMemberUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultUpdateCastMemberUseCase

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        val expectedId = aMember.id
        val expectedName = Fixture.name()
        val expectedType = CastMemberType.ACTOR

        val aCommand = UpdateCastMemberCommand.with(expectedId.value, expectedName, expectedType)

        every { castMemberGateway.findById(any()) } returns aMember
        every { castMemberGateway.update(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        verify { castMemberGateway.findById(expectedId) }

        verify {
            castMemberGateway.update(
                withArg {
                    assertEquals(expectedId, it.id)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                    assertEquals(aMember.createdAt, it.createdAt)
                    assertTrue(aMember.updatedAt.isBefore(it.updatedAt))
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        val expectedId = aMember.id
        val expectedName = "   "
        val expectedType = Fixture.CastMembers.type()
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"

        val aCommand = UpdateCastMemberCommand.with(expectedId.value, expectedName, expectedType)

        every { castMemberGateway.findById(any()) } returns aMember

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify { castMemberGateway.findById(expectedId) }

        verify(exactly = 0) { castMemberGateway.update(any()) }
    }

    @Test
    fun givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedName = "   "
        val expectedType = Fixture.CastMembers.type()
        val expectedErrorMessage = "CastMember with ID 123 was not found"

        val aCommand = UpdateCastMemberCommand.with(expectedId.value, expectedName, expectedType)

        every { castMemberGateway.findById(any()) } returns null

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorMessage, actualException.message)

        verify { castMemberGateway.findById(expectedId) }

        verify(exactly = 0) { castMemberGateway.update(any()) }
    }
}
