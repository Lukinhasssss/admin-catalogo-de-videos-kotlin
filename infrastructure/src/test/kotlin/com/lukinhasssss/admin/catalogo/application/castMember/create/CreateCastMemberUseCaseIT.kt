package com.lukinhasssss.admin.catalogo.application.castMember.create

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class CreateCastMemberUseCaseIT {

    @Autowired
    private lateinit var useCase: CreateCastMemberUseCase

    @SpykBean
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun givenAValidCommand_whenCallsCreateCastMember_shoulReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aCommand = CreateCastMemberCommand.with(expectedName, expectedType)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertNotNull(actualOutput.id)

        val actualMember = castMemberRepository.findById(actualOutput.id).get()

        with(actualMember) {
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
        }

        verify { castMemberGateway.create(any()) }
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        // given
        val expectedName = "   "
        val expectedType = Fixture.CastMember.type()
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
