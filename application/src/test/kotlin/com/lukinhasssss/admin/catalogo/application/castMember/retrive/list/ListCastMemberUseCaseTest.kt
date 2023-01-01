package com.lukinhasssss.admin.catalogo.application.castMember.retrive.list

import com.lukinhasssss.admin.catalogo.application.Fixture
import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ListCastMemberUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultListCastMemberUseCase

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @Test
    fun givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        // given
        val members = listOf(
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        )

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = members.size.toLong()
        val expectedItems = members.map { CastMemberListOutput.from(it) }

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, expectedTotal, members)

        every { castMemberGateway.findAll(aQuery) } returns expectedPagination

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
        val members = listOf<CastMember>()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0L
        val expectedItems = listOf<CastMemberListOutput>()

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, expectedTotal, members)

        every { castMemberGateway.findAll(aQuery) } returns expectedPagination

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

        verify(exactly = 1) { castMemberGateway.findAll(aQuery) }
    }
}
