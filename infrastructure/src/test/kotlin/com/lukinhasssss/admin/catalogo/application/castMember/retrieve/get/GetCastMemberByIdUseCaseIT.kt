package com.lukinhasssss.admin.catalogo.application.castMember.retrieve.get

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.DefaultGetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class GetCastMemberByIdUseCaseIT {

    @Autowired
    private lateinit var useCase: DefaultGetCastMemberByIdUseCase

    @SpykBean
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aMember = CastMember.newMember(expectedName, expectedType)

        val expectedId = aMember.id

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        val actualCastMember = useCase.execute(expectedId.value)

        // then
        with(actualCastMember) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertEquals(aMember.updatedAt, updatedAt)
        }

        verify(exactly = 1) { castMemberGateway.findById(any()) }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFound() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedErrorMessage = "CastMember with ID ${expectedId.value} was not found"

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(expectedId.value) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { castMemberGateway.findById(any()) }
    }
}
