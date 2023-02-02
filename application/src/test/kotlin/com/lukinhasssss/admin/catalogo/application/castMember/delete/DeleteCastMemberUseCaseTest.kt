package com.lukinhasssss.admin.catalogo.application.castMember.delete

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DeleteCastMemberUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultDeleteCastMemberUseCase

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @Test
    fun givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())

        val expectedId = aMember.id

        every { castMemberGateway.deleteById(any()) } returns Unit

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        verify(exactly = 1) { castMemberGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        val expectedId = CastMemberID.from("any")

        every { castMemberGateway.deleteById(any()) } returns Unit

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        verify(exactly = 1) { castMemberGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())

        val expectedId = aMember.id

        every { castMemberGateway.deleteById(any()) } throws IllegalStateException("Gateway error")

        // when
        assertThrows<IllegalStateException> { useCase.execute(expectedId.value) }

        verify(exactly = 1) { castMemberGateway.deleteById(expectedId) }
    }
}
