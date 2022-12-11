package com.lukinhasssss.admin.catalogo.application.castMember.retrive.get

import com.lukinhasssss.admin.catalogo.application.Fixture
import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetCastMemberByIdUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultGetCastMemberByIdUseCase

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @Test
    fun givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMember.type()

        val aMember = CastMember.newMember(expectedName, expectedType)

        val expectedId = aMember.id

        every { castMemberGateway.findById(any()) } returns aMember

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

        verify(exactly = 1) { castMemberGateway.findById(expectedId) }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFound() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedErrorMessage = "CastMember with ID ${expectedId.value} was not found"

        every { castMemberGateway.findById(expectedId) } returns null

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(expectedId.value) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { castMemberGateway.findById(expectedId) }
    }
}
