package com.lukinhasssss.admin.catalogo.infrastructure.castMember

import com.lukinhasssss.admin.catalogo.Fixture
import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
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
        val expectedType = Fixture.CastMember.type()

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
}
