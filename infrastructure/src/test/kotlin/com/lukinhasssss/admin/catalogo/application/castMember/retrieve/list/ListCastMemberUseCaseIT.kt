package com.lukinhasssss.admin.catalogo.application.castMember.retrieve.list

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.CastMemberListOutput
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.DefaultListCastMemberUseCase
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ListCastMemberUseCaseIT {

    @Autowired
    private lateinit var useCase: DefaultListCastMemberUseCase

    @SpykBean
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        // given
        val members = listOf(
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        )

        castMemberRepository.saveAllAndFlush(members.map { CastMemberJpaEntity.from(it) })

        assertEquals(2, castMemberRepository.count())

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = members.size.toLong()
        val expectedItems = members.map { CastMemberListOutput.from(it) }

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }

        verify(exactly = 1) { castMemberGateway.findAll(aQuery) }
    }

    @Test
    fun givenAValidQuery_whenCallsListCastMemberAndResultIsEmpty_shouldReturnAll() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0L
        val expectedItems = listOf<CastMemberListOutput>()

        assertEquals(0, castMemberRepository.count())

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }

        verify(exactly = 1) { castMemberGateway.findAll(any()) }
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_thenReturnException() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway error"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        every { castMemberGateway.findAll(aQuery) } throws IllegalStateException(expectedErrorMessage)

        // when
        val actualException = assertThrows<IllegalStateException> { useCase.execute(aQuery) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { castMemberGateway.findAll(any()) }
    }
}
