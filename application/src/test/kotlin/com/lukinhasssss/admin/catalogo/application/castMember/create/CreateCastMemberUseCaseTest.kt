package com.lukinhasssss.admin.catalogo.application.castMember.create

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateCastMemberUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultCreateCastMemberUseCase

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @Test
    fun givenAValidCommand_whenCallsCreateCastMember_shoulReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMembers.type()

        val aCommand = CreateCastMemberCommand.with(expectedName, expectedType)

        every { castMemberGateway.create(any()) } answers { firstArg() }

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertNotNull(actualOutput.id)

        verify {
            castMemberGateway.create(
                withArg {
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedType, it.type)
                    assertNotNull(it.createdAt)
                    assertNotNull(it.updatedAt)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        // given
        val expectedName = "   "
        val expectedType = Fixture.CastMembers.type()
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"

        val aCommand = CreateCastMemberCommand.with(expectedName, expectedType)

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify(exactly = 0) { castMemberGateway.create(any()) }
    }
}
