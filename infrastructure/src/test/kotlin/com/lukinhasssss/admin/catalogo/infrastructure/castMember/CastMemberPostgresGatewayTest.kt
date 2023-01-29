package com.lukinhasssss.admin.catalogo.infrastructure.castMember

import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired

@PostgresGatewayTest
class CastMemberPostgresGatewayTest {

    @Autowired
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun testDependencies() {
        assertNotNull(castMemberGateway)
        assertNotNull(castMemberRepository)
    }

    @Test
    fun givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMembers.type()

        val aMember = CastMember.newMember(expectedName, expectedType)

        val expectedId = aMember.id

        assertEquals(0, castMemberRepository.count())

        // when
        val actualMember = castMemberGateway.create(aMember)

        // then
        assertEquals(1, castMemberRepository.count())

        with(actualMember) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertEquals(aMember.updatedAt, updatedAt)
        }

        val persistedMember = castMemberRepository.findById(expectedId.value).get()

        with(persistedMember) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertEquals(aMember.updatedAt, updatedAt)
        }
    }

    @Test
    fun givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = CastMemberType.ACTOR

        val aMember = CastMember.newMember("A Pedra", CastMemberType.DIRECTOR)

        val expectedId = aMember.id

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        val actualMember = castMemberGateway.update(
            aMember.update(expectedName, expectedType)
        )

        // then
        assertEquals(1, castMemberRepository.count())

        with(actualMember) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertTrue(aMember.updatedAt.isBefore(updatedAt))
        }

        val persistedMember = castMemberRepository.findById(expectedId.value).get()

        with(persistedMember) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertTrue(aMember.updatedAt.isBefore(updatedAt))
        }
    }

    @Test
    fun givenAPrePersistedCastMember_whenCallsDeleteById_shouldDeleteIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        castMemberGateway.deleteById(aMember.id)

        // then
        assertEquals(0, castMemberRepository.count())
    }

    @Test
    fun givenAnInvalidCastMemberId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        castMemberGateway.deleteById(CastMemberID.from("123"))

        // then
        assertEquals(1, castMemberRepository.count())
    }

    @Test
    fun givenAPrePersistedCastMember_whenCallsFindById_shouldReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.CastMembers.type()

        val aMember = CastMember.newMember(expectedName, expectedType)

        val expectedId = aMember.id

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        val actualCastMember = castMemberGateway.findById(expectedId)

        // then
        with(actualCastMember!!) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertEquals(aMember.updatedAt, updatedAt)
        }
    }

    @Test
    fun givenAnInvalidCastMemberId_whenCallsFindById_shouldReturnNull() {
        // given
        val expectedId = CastMemberID.from("any")

        val aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember))

        assertEquals(1, castMemberRepository.count())

        // when
        val actualCastMember = castMemberGateway.findById(expectedId)

        // then
        assertNull(actualCastMember)
    }

    @Test
    fun givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTotal = 0

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = castMemberGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal.toLong(), total)
            assertEquals(expectedTotal, items.size)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "vin, 0, 10, 1, 1, Vin Diesel",
        "taran, 0, 10, 1, 1, Quentin Tarantino",
        "jas, 0, 10, 1, 1, Jason Momoa",
        "har, 0, 10, 1, 1, Kit Harington",
        "MAR, 0, 10, 1, 1, Martin Scorsese"
    )
    fun givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
        expectedTerms: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedMemberName: String
    ) {
        // given
        mockMembers()

        val expectedSort = "name"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = castMemberGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedMemberName, items[0].name)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "name, asc, 0, 10, 5, 5, Jason Momoa",
        "name, desc, 0, 10, 5, 5, Vin Diesel",
        "createdAt, asc, 0, 10, 5, 5, Kit Harington",
        "createdAt, desc, 0, 10, 5, 5, Martin Scorsese"
    )
    fun givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
        expectedSort: String,
        expectedDirection: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedMemberName: String
    ) {
        // given
        mockMembers()

        val expectedTerms = ""

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = castMemberGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedMemberName, items[0].name)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "0, 2, 2, 5, Jason Momoa;Kit Harington",
        "1, 2, 2, 5, Martin Scorsese;Quentin Tarantino",
        "2, 2, 1, 5, Vin Diesel"
    )
    fun givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCastMembers: String
    ) {
        // given
        mockMembers()

        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualPage = castMemberGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)

            for ((index, expectedMemberName) in expectedCastMembers.split(";").withIndex()) {
                val actualGenreName = items[index].name
                assertEquals(expectedMemberName, actualGenreName)
            }
        }
    }

    private fun mockMembers() {
        castMemberRepository.saveAllAndFlush(
            listOf(
                CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
            )
        )
    }
}
