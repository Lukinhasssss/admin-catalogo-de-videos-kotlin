package com.lukinhasssss.admin.catalogo.application.castMember.update

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class UpdateCastMemberUseCaseIT {

    @Autowired
    private lateinit var useCase: UpdateCastMemberUseCase

    @SpykBean
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        val expectedId = aMember.id
        val expectedName = Fixture.name()
        val expectedType = CastMemberType.ACTOR

        val aCommand = UpdateCastMemberCommand.with(expectedId.value, expectedName, expectedType)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)

        assertEquals(1, castMemberRepository.count())

        val actualMember = castMemberRepository.findById(actualOutput.id).get()

        with(actualMember) {
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertTrue(aMember.updatedAt.isBefore(updatedAt))
        }

        verify { castMemberGateway.findById(any()) }
        verify { castMemberGateway.update(any()) }
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        val expectedId = aMember.id
        val expectedName = "   "
        val expectedType = Fixture.CastMember.type()
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

        verify { castMemberGateway.findById(any()) }

        verify(exactly = 0) { castMemberGateway.update(any()) }
    }

    @Test
    fun givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // given
        val aMember = CastMember.newMember("Any Name", CastMemberType.DIRECTOR)

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        val expectedId = CastMemberID.from("123")
        val expectedName = "   "
        val expectedType = Fixture.CastMember.type()
        val expectedErrorMessage = "CastMember with ID 123 was not found"

        val aCommand = UpdateCastMemberCommand.with(expectedId.value, expectedName, expectedType)

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorMessage, actualException.message)

        assertEquals(1, castMemberRepository.count())

        verify { castMemberGateway.findById(any()) }
        verify(exactly = 0) { castMemberGateway.update(any()) }
    }
}
