package com.lukinhasssss.admin.catalogo.application.castMember.delete

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class DeleteCastMemberUseCaseIT {

    @Autowired
    private lateinit var useCase: DeleteCastMemberUseCase

    @SpykBean
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        val aMemberTwo = CastMember.newMember(Fixture.name(), Fixture.CastMember.type())

        val expectedId = aMember.id

        castMemberRepository.saveAllAndFlush(
            listOf(
                CastMemberJpaEntity.from(aMember),
                CastMemberJpaEntity.from(aMemberTwo)
            )
        )

        assertEquals(2, castMemberRepository.count())

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        assertEquals(1, castMemberRepository.count())
        assertFalse(castMemberRepository.existsById(expectedId.value))
        assertTrue(castMemberRepository.existsById(aMemberTwo.id.value))

        verify(exactly = 1) { castMemberGateway.deleteById(any()) }
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type())

        val expectedId = CastMemberID.from("any")

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        assertEquals(1, castMemberRepository.count())

        verify(exactly = 1) { castMemberGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type())

        val expectedId = aMember.id

        every { castMemberGateway.deleteById(any()) } throws IllegalStateException("Gateway error")

        // when
        assertThrows<IllegalStateException> { useCase.execute(expectedId.value) }

        verify(exactly = 1) { castMemberGateway.deleteById(expectedId) }
    }
}
